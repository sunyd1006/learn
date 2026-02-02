"""
publishers/toutiao.py — ToutiaoPublisher

Full Playwright-based implementation of the Toutiao (今日头条) article
publishing workflow, ported from the verified test_login.py.

Login strategy:
  1. If a cached session state file exists AND force=False:
       - Load the state into the browser context.
       - Navigate to the index page and check if we're truly logged in.
       - If still on login page → cache is stale, fall through to fresh login.
  2. Fresh login:
       - If use_password=True and creds available → auto fill username/password.
       - Otherwise → open browser and wait for the user to log in manually.
  3. After successful login, save the session state to auth/toutiao_state.json.
"""

from __future__ import annotations

import os
import time

import dotenv
from playwright.sync_api import BrowserContext, Page, sync_playwright

from publishers.base import Article, BasePublisher

# ---------------------------------------------------------------------------
# Constants
# ---------------------------------------------------------------------------

LOGIN_URL = "https://mp.toutiao.com/auth/page/login"
INDEX_URL = "https://mp.toutiao.com/profile_v4/index"
PUBLISH_URL = "https://mp.toutiao.com/profile_v4/graphic/publish"


class ToutiaoPublisher(BasePublisher):
    """Playwright-based publisher for 今日头条 MP (mp.toutiao.com)."""

    PLATFORM = "toutiao"

    def __init__(self, headless: bool = False):
        super().__init__(headless=headless)
        dotenv.load_dotenv()
        self._playwright = None
        self._browser = None
        self._context: BrowserContext | None = None
        self._page: Page | None = None

    # ------------------------------------------------------------------
    # Public interface
    # ------------------------------------------------------------------

    def login(self, force: bool = False, use_password: bool = False) -> None:
        """
        Ensure the browser is logged in to Toutiao MP.

        See module docstring for the full login strategy.
        """
        self._ensure_browser()

        if not force and self._has_cached_state():
            self.logger.info("Found cached session at %s — attempting to reuse.", self.state_path)
            reused = self._try_reuse_session()
            if reused:
                self.logger.info("✅ Cached session is valid — skipping login.")
                return
            self.logger.warning(
                "Cached session is stale or invalid. Falling back to fresh login."
            )

        # Fresh login
        self._fresh_login(use_password=use_password)

    def publish(self, article: Article) -> bool:
        """Execute the full article publishing workflow."""
        if self._page is None:
            raise RuntimeError("Not logged in. Call login() first.")

        page = self._page
        docx_path = article.resolve_docx()
        self.logger.info("Starting publish flow. Article: '%s'", article.title)

        # 1. Navigate to publish page
        self.logger.info("Navigating to publish page…")
        page.goto(PUBLISH_URL)
        page.wait_for_load_state("domcontentloaded")
        page.wait_for_selector(".syl-toolbar-tool", timeout=15000)

        # 2. Dismiss overlays (AI assistant drawer / guide dialogs)
        self._dismiss_overlays(page)

        # 3. Import docx
        self.logger.info("Importing docx: %s", docx_path)
        self._import_docx(page, docx_path)

        # 4. Configure publish settings
        self.logger.info("Configuring publish settings…")
        self._configure_cover(page, article.cover)
        if article.first_release:
            self._click_label(page, "头条首发")
            self.logger.info("Checked: 头条首发")
        self._configure_declarations(page, article.declarations)

        # 5. Submit
        self.logger.info("Clicking 预览并发布…")
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)")
        time.sleep(1)
        self._js_click_button(page, "预览并发布", css="button.publish-btn-last")

        # 6. Confirm publish dialog
        try:
            page.get_by_text("确认发布").click(timeout=10000)
            self.logger.info("✅ Publish confirmed.")
        except Exception:
            self.logger.warning("'确认发布' dialog did not appear — may have published directly.")

        # 7. Save updated session state
        self._save_state()
        self.logger.info("Session state saved to %s", self.state_path)
        return True

    def close(self) -> None:
        """Release all Playwright resources."""
        try:
            if self._context:
                self._context.close()
            if self._browser:
                self._browser.close()
            if self._playwright:
                self._playwright.stop()
        except Exception as exc:
            self.logger.debug("Error during close: %s", exc)
        finally:
            self._page = None
            self._context = None
            self._browser = None
            self._playwright = None

    # ------------------------------------------------------------------
    # Internal helpers
    # ------------------------------------------------------------------

    def _ensure_browser(self) -> None:
        """Start Playwright and open a fresh browser + context (no stored state)."""
        if self._playwright is not None:
            return
        self._playwright = sync_playwright().start()
        self._browser = self._playwright.chromium.launch(headless=self.headless)
        os.makedirs(os.path.dirname(self.state_path), exist_ok=True)
        self._context = self._browser.new_context()
        self._page = self._context.new_page()

    def _try_reuse_session(self) -> bool:
        """
        Load cached session into a new context and navigate to INDEX_URL.
        Returns True if the page ends up at the dashboard (not the login page).
        """
        try:
            # Replace current context with one that has stored state
            if self._context:
                self._context.close()
            self._context = self._browser.new_context(
                storage_state=self.state_path
            )
            self._page = self._context.new_page()
            self._page.goto(INDEX_URL)
            self._page.wait_for_load_state("networkidle", timeout=15000)
            current_url = self._page.url
            if "login" in current_url:
                self.logger.info("Redirected to login page — session cache is invalid.")
                return False
            self.logger.info("Session reuse successful (current URL: %s).", current_url)
            return True
        except Exception as exc:
            self.logger.warning("Session reuse attempt failed: %s", exc)
            return False

    def _fresh_login(self, use_password: bool) -> None:
        """Perform a fresh interactive or password-based login."""
        page = self._page
        self.logger.info("Navigating to login page: %s", LOGIN_URL)
        page.goto(LOGIN_URL)
        page.wait_for_load_state("networkidle")

        username = os.getenv("TOUTIAO_USERNAME")
        password = os.getenv("TOUTIAO_PASSWORD")

        if use_password and username and password:
            self.logger.info("Attempting automated password login for user: %s", username)
            try:
                page.click("li[aria-label='账密登录']", timeout=5000)
                page.wait_for_selector("input[aria-label='请输入手机号或邮箱']", timeout=5000)
                page.fill("input[aria-label='请输入手机号或邮箱']", username)
                page.fill("input[aria-label='请输入密码']", password)
                page.click("span[aria-label='协议勾选框']")
                page.click("button.web-login-button")
                self.logger.info("Credentials submitted. Waiting for redirect…")
                try:
                    page.wait_for_url(INDEX_URL, timeout=30000)
                    self.logger.info("✅ Password login successful.")
                    self._save_state()
                    return
                except Exception:
                    self.logger.warning(
                        "Auto-redirect did not occur in time. May need manual CAPTCHA."
                    )
            except Exception as exc:
                self.logger.error("Password login automation failed: %s", exc)
        else:
            if use_password:
                self.logger.warning(
                    "use_password=True but TOUTIAO_USERNAME/PASSWORD not set in .env. "
                    "Falling back to manual login."
                )
            else:
                self.logger.info("Manual login mode — awaiting user action in browser.")

        # Manual fallback: wait for user to log in
        print("\n" + "=" * 60)
        print("请在浏览器中手动完成登录（扫码、验证码 或 账号密码均可）。")
        print("成功进入后台（URL 含 /profile_v4）后，回到终端按【回车键】继续…")
        print("=" * 60 + "\n")
        input()
        self._save_state()
        self.logger.info("Manual login complete. Session state saved.")

    # ------------------------------------------------------------------
    # Publish sub-steps
    # ------------------------------------------------------------------

    def _dismiss_overlays(self, page: Page) -> None:
        """Hide AI assistant drawer and any guide dialogs via JS."""
        count = page.evaluate("""
            () => {
                let n = 0;
                ['.byte-drawer-mask', '.byte-drawer-wrapper',
                 '.ai-assistant-drawer', '.publish-assistant-wrapper']
                  .forEach(sel => {
                      document.querySelectorAll(sel).forEach(el => {
                          el.style.display = 'none';
                          el.style.pointerEvents = 'none';
                          n++;
                      });
                  });
                const guide = Array.from(document.querySelectorAll('button'))
                    .find(b => b.textContent.includes('我知道了'));
                if (guide) { guide.click(); n++; }
                return n;
            }
        """)
        self.logger.debug("Dismissed %d overlay elements.", count)

    def _import_docx(self, page: Page, docx_path: str) -> None:
        """Click the doc-import toolbar button and upload the .docx file."""
        page.evaluate(
            "document.querySelector('.syl-toolbar-tool.doc-import button').click()"
        )
        self.logger.debug("Clicked doc-import toolbar button.")
        page.wait_for_selector("input[type='file']", state="attached", timeout=10000)
        page.locator("input[type='file']").set_input_files(docx_path)
        self.logger.debug("File selected: %s — waiting for parse…", docx_path)
        time.sleep(3)  # Allow the DOCX parser to populate the editor

    def _configure_cover(self, page: Page, cover: str) -> None:
        """Select the specified cover option (e.g. '无封面', '单图', '三图')."""
        self._click_label(page, cover)
        self.logger.info("Cover set to: %s", cover)

    def _configure_declarations(self, page: Page, declarations: list[str]) -> None:
        """Check the specified work-declaration checkboxes."""
        js = """
            (labels) => {
                labels.forEach(text => {
                    const el = Array.from(document.querySelectorAll('label'))
                        .find(l => l.textContent.includes(text));
                    if (el) el.click();
                });
            }
        """
        page.evaluate(js, declarations)
        self.logger.info("Declarations set: %s", declarations)

    @staticmethod
    def _click_label(page: Page, text: str) -> None:
        """Find a <label> containing `text` and click it via JS (bypasses pointer-events)."""
        page.evaluate(
            """(text) => {
                const el = Array.from(document.querySelectorAll('label'))
                    .find(l => l.textContent.includes(text));
                if (el) el.click();
            }""",
            text,
        )

    @staticmethod
    def _js_click_button(page: Page, text: str, css: str = "") -> None:
        """Click a <button> by CSS selector or by text content, via JS."""
        page.evaluate(
            """([css, text]) => {
                const btn = (css ? document.querySelector(css) : null)
                    || Array.from(document.querySelectorAll('button'))
                        .find(b => b.textContent.includes(text));
                if (btn) btn.click();
            }""",
            [css, text],
        )

    def _save_state(self) -> None:
        """Persist the current browser session state to disk."""
        os.makedirs(os.path.dirname(self.state_path), exist_ok=True)
        self._context.storage_state(path=self.state_path)

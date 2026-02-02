"""
test/test_info_publisher.py — Automated integration test for info_publisher.

Tests the full Toutiao publish pipeline:
  1. Reports whether cached session state exists and attempts reuse.
  2. Falls back to password login (--password-login) if no cache or cache is stale.
  3. Runs publish() with articles/file1.docx.
  4. Asserts success and logs result.

Run:
    uv run python test/test_info_publisher.py
    # or with forced fresh password login:
    uv run python test/test_info_publisher.py --force-login
"""

from __future__ import annotations

import argparse
import logging
import os
import sys

# Make sure the project root is on the path when running from test/
sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))

from publishers import Article, ToutiaoPublisher

# ---------------------------------------------------------------------------
# Logging setup (mirrors info_publisher.py)
# ---------------------------------------------------------------------------

LOG_DIR = os.path.join(os.path.dirname(__file__), "..", "logs")
os.makedirs(LOG_DIR, exist_ok=True)

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s | %(levelname)-8s | %(name)s | %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
    handlers=[
        logging.StreamHandler(sys.stdout),
        logging.FileHandler(
            os.path.join(LOG_DIR, "test_publisher.log"),
            encoding="utf-8",
        ),
    ],
)

logger = logging.getLogger("test.info_publisher")


# ---------------------------------------------------------------------------
# Test
# ---------------------------------------------------------------------------

def run_test(force_login: bool = False) -> bool:
    """
    Run the full publish integration test.
    Returns True on success, False on failure.
    """
    publisher = ToutiaoPublisher(headless=False)

    # ---- Session-cache diagnostics ----------------------------------------
    if publisher._has_cached_state():
        logger.info(
            "📦 Cached session found at: %s — will attempt reuse.",
            publisher.state_path,
        )
    else:
        logger.info(
            "🔑 No cached session found at: %s — fresh login required.",
            publisher.state_path,
        )

    article = Article(
        title="acb",
        docx_path=os.path.join(
            os.path.dirname(__file__), "..", "articles", "file1.docx"
        ),
        cover="无封面",
        first_release=True,
        declarations=["取材网络", "引用AI"],
    )

    try:
        # Use password-login as default for automated tests so no human intervention
        # is needed (assuming .env has credentials).
        publisher.login(force=force_login, use_password=True)

        # ---- Check if session reuse actually worked -----------------------
        current_url = publisher._page.url if publisher._page else ""
        if "login" in current_url:
            logger.warning("⚠️  Still on login page after login() — something may be wrong.")
        else:
            logger.info("✅ Login confirmed (URL: %s)", current_url)

        # ---- Publish -------------------------------------------------------
        success = publisher.publish(article)

        if success:
            logger.info("✅ [PASS] 文章发布成功")
        else:
            logger.error("❌ [FAIL] publish() returned False")

        return success

    except Exception as exc:
        logger.exception("❌ [ERROR] Test failed with exception: %s", exc)
        return False

    finally:
        publisher.close()


def main() -> int:
    parser = argparse.ArgumentParser(description="Automated test for info_publisher")
    parser.add_argument(
        "--force-login",
        action="store_true",
        help="Ignore cached session and force a fresh login",
    )
    args = parser.parse_args()

    passed = run_test(force_login=args.force_login)
    return 0 if passed else 1


if __name__ == "__main__":
    sys.exit(main())

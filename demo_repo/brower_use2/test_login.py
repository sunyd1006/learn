import os
import time
from dotenv import load_dotenv
from playwright.sync_api import sync_playwright

# 加载环境变量
load_dotenv()


def test_login():
    # 确保 auth 目录存在
    auth_dir = os.path.join(os.getcwd(), 'auth')
    if not os.path.exists(auth_dir):
        os.makedirs(auth_dir)

    state_path = os.path.join(auth_dir, 'toutiao_state.json')

    with sync_playwright() as p:
        # 启动 Chromium 浏览器
        browser = p.chromium.launch(headless=False)

        # 创建新上下文
        context = browser.new_context()

        # 创建新页面
        page = context.new_page()

        print("正在打开今日头条登录页...")
        page.goto("https://mp.toutiao.com/auth/page/login")

        # 等待页面加载
        page.wait_for_load_state("networkidle")

        # 获取凭据
        username = os.getenv("TOUTIAO_USERNAME")
        password = os.getenv("TOUTIAO_PASSWORD")

        if username and password:
            print("尝试自动填写账号密码...")
            try:
                # 点击“账密登录”标签
                # 根据之前的探测，选择器为 li[aria-label='账密登录']
                page.click("li[aria-label='账密登录']", timeout=5000)

                # 等待输入框出现
                page.wait_for_selector("input[aria-label='请输入手机号或邮箱']", timeout=5000)

                # 输入账号内容
                page.fill("input[aria-label='请输入手机号或邮箱']", username)
                page.fill("input[aria-label='请输入密码']", password)

                # 勾选协议
                page.click("span[aria-label='协议勾选框']")

                # 点击登录
                page.click("button.web-login-button")

                print("已提交登录，请检查是否需要处理验证码...")

                # 等待跳转到首页，说明登录成功
                print("正在等待跳转到创作者中心首页...")
                try:
                    page.wait_for_url("https://mp.toutiao.com/profile_v4/index", timeout=30000)
                    print("检测到已成功跳转到首页！")
                except Exception:
                    print("未能在预期时间内自动跳转，可能需要手动处理验证码或登录。")

            except Exception as e:
                print(f"自动化执行过程中遇到错误: {e}")
                print("请通过手动操作继续。")
        else:
            print("未在 .env 中找到账号密码，请手动登录。")

        print("\n" + "="*50)
        print("请在浏览器中确认登录状态（如有验证码请手动完成）。")
        print("登录成功并在 URL 中看到 /profile_v4/index 后，请回到这里按下【回车键】保存状态...")
        print("="*50 + "\n")

        print("\n正在由首页跳转到发布页面...")
        page.goto("https://mp.toutiao.com/profile_v4/graphic/publish")
        page.wait_for_load_state("domcontentloaded")
        # 等待编辑器工具栏出现，表示页面真正可交互
        page.wait_for_selector(".syl-toolbar-tool", timeout=15000)


        # -------------------------------------------------------
        # 处理遮挡：用 JS 直接隐藏 AI 助手抽屉及其全屏蒙层
        # 原因：byte-drawer-mask 是一个覆盖全页面的 div，会拦截所有后续的 Playwright 点击
        # -------------------------------------------------------
        dismissed = page.evaluate("""
            () => {
                let count = 0;
                // 隐藏所有抽屉相关元素（遮罩层 + 抽屉容器）
                const selectors = [
                    '.byte-drawer-mask',
                    '.byte-drawer-wrapper',
                    '.ai-assistant-drawer',
                    '.publish-assistant-wrapper'
                ];
                selectors.forEach(sel => {
                    document.querySelectorAll(sel).forEach(el => {
                        el.style.display = 'none';
                        el.style.pointerEvents = 'none';
                        count++;
                    });
                });
                // 同时关闭引导弹窗（如果有）
                const guideBtn = Array.from(document.querySelectorAll('button'))
                    .find(b => b.textContent.includes('我知道了'));
                if (guideBtn) { guideBtn.click(); count++; }
                return count;
            }
        """)
        print(f"已通过 JS 隐藏 {dismissed} 个遮挡元素（AI 助手抽屉/蒙层）")

        print("正在填写文章信息...")

        # Step 1: 用 JavaScript 点击导入按钮，绕过 AI 助手抽屉的遮挡
        # 注意：不能用 force=True 的 Playwright click，因为 JS click 更可靠地绕过覆盖层
        page.evaluate("document.querySelector('.syl-toolbar-tool.doc-import button').click()")
        print("已点击导入按钮，等待文档导入 Modal 出现...")

        # Step 2: 等待文档导入弹窗出现（选择文档按钮 或 文件 input）
        # Modal 中有 .doc-import-btn 按钮和旁边的 input[type='file']
        page.wait_for_selector("input[type='file']", state="attached", timeout=10000)
        print("Modal 已出现，开始设置文件...")

        # Step 3: 直接对 Modal 内的隐藏 input 设置文件（无需点击选择按钮）
        page.locator("input[type='file']").set_input_files(
            "articles/人工智能技术发展报告.docx"
        )
        print("已选择文件，等待导入解析完成...")
        time.sleep(3)  # 等待 docx 解析并填入编辑器

        print("正在配置发布选项...")
        # 全部使用 JS 点击，彻底绕过 Playwright 指针事件检测
        # （因为即使隐藏了蒙层，Playwright 的可操作性检查仍可能失败）

        # 3. 封面选择：无封面
        page.evaluate("""
            () => {
                const label = Array.from(document.querySelectorAll('label'))
                    .find(l => l.textContent.includes('无封面'));
                if (label) label.click();
            }
        """)
        print("已选择：无封面")

        # 4. 投放广告：默认选中，不额外操作

        # 5. 声明首发：头条首发
        page.evaluate("""
            () => {
                const label = Array.from(document.querySelectorAll('label'))
                    .find(l => l.textContent.includes('头条首发'));
                if (label) label.click();
            }
        """)
        print("已勾选：头条首发")

        # 6. 作品声明：取材网络 + 引用AI
        page.evaluate("""
            () => {
                // 取材网络 (value=4)
                const network = Array.from(document.querySelectorAll('label'))
                    .find(l => l.textContent.includes('取材网络'));
                if (network) network.click();
                // 引用AI (value=3)
                const ai = Array.from(document.querySelectorAll('label'))
                    .find(l => l.textContent.includes('引用AI') || l.textContent.includes('引用 AI'));
                if (ai) ai.click();
            }
        """)
        print("已勾选：取材网络 + 引用AI")

        print("配置完成，准备点击预览并发布...")

        # 滚动到底部确保按钮可见
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)")
        time.sleep(1)

        # 7. 点击预览并发布
        page.evaluate("""
            () => {
                const btn = document.querySelector('button.publish-btn-last')
                    || Array.from(document.querySelectorAll('button'))
                        .find(b => b.textContent.includes('预览并发布'));
                if (btn) btn.click();
            }
        """)
        print("已点击：预览并发布")


        print("已点击“预览并发布”。")

        page.get_by_text("确认发布").click()

        print("\n" + "="*50)
        print("自动化发布流程已模拟执行。")
        print("请在浏览器中确认最终状态。完成后回到这里按【回车键】保存状态并退出...")
        print("="*50 + "\n")

        input("按回车保存状态并结束...")

        # 保存登录状态
        context.storage_state(path=state_path)
        print(f"登录状态已成功保存至: {state_path}")

        browser.close()



if __name__ == "__main__":
    test_login()

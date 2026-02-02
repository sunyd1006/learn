import os
import asyncio
import json
import argparse
from pathlib import Path
from dotenv import load_dotenv
from browser_use import Agent, Browser, BrowserProfile, ChatOpenAI

# 加载环境变量
load_dotenv()

# 配置
STATE_FILE = os.path.join(os.getcwd(), 'auth', 'toutiao_state.json')
LOGIN_URL = "https://mp.toutiao.com/auth/page/login"
PUBLISH_URL = "https://mp.toutiao.com/profile_v4/graphic/publish"

def get_llm(model_name_arg=None):
    """从环境变量初始化 LLM"""
    api_key = os.getenv("OPENAI_API_KEY")
    base_url = os.getenv("OPENAI_BASE_URL")
    # 优先级：命令行参数 > 环境配置 > 默认值
    model_name = model_name_arg or os.getenv("OPENAI_MODEL_NAME", "gpt-4o")

    if not api_key:
        raise ValueError("请在 .env 中配置 OPENAI_API_KEY")

    print(f"初始化 LLM: {model_name} (BaseURL: {base_url or 'Default(OpenAI)'})")

    return ChatOpenAI(
        model=model_name,
        api_key=api_key,
        base_url=base_url,
        # 兼容性设置：许多三方 provider (如 Ark/DeepSeek) 可能不支持 json_schema 模式
        dont_force_structured_output=True,
        add_schema_to_system_prompt=True,
        remove_min_items_from_schema=True
    )

async def perform_login(model_name=None):
    """尝试自动填充登录并引导用户完成（处理验证码）"""
    llm = get_llm(model_name)
    # 在 0.12.1 中，Browser 就是 BrowserSession
    browser = Browser(headless=False)

    username = os.getenv("TOUTIAO_USERNAME")
    password = os.getenv("TOUTIAO_PASSWORD")

    if not username or not password:
        print("警告: .env 中未配置 TOUTIAO_USERNAME 或 TOUTIAO_PASSWORD，无法执行自动登录。")
        print("将仅打开浏览器由您手动登录。")

    print(f"正在启动登录 Agent，目标: {LOGIN_URL}")

    # 定义登录任务
    login_task = f"""
    1. 访问今日头条登录页: {LOGIN_URL}
    2. 如果当前不是密码登录界面，请寻找并点击“密码登录”按钮。
    3. 在账号/手机号输入框中输入: "{username}"
    4. 在密码输入框中输入: "{password}"
    5. 寻找并勾选“我已阅读并同意...”相关的服务协议/隐私政策勾选框。
    6. 点击“登录”按钮。
    7. 如果出现滑块、拼图或其他验证码，请尝试观察并告知用户，或者如果操作简单请尝试解决。
    8. 登录成功后（看到后台页面），请说“登录操作已完成”。
    """

    agent = Agent(
        task=login_task,
        llm=llm,
        browser=browser
    )

    try:
        print("AI 正在尝试为您执行登录操作...")
        await agent.run()

        print("\n" + "="*50)
        print("【人工确认】AI 已尝试执行登录操作。")
        print("如果还有验证码未完成或登录未成功，请在浏览器中手动完成。")
        print("完成后（看到创作者中心后台），请回到终端按【回车键】继续保存状态...")
        print("="*50 + "\n")

        # 等待用户在终端按回车
        await asyncio.get_event_loop().run_in_executor(None, input)

        # 保存状态
        os.makedirs(os.path.dirname(STATE_FILE), exist_ok=True)
        # 导出状态到文件
        await browser.export_storage_state(STATE_FILE)

        print(f"登录成功！状态已保存至: {STATE_FILE}")
    finally:
        await browser.stop()

async def run_publisher(title, content, model_name=None):
    """执行发布文章的 Agent 任务"""
    llm = get_llm(model_name)

    # 如果状态文件不存在，先引导登录
    if not os.path.exists(STATE_FILE):
        print("未发现登录状态文件，正在启动登录流程...")
        await perform_login(model_name)

    # 定义 Agent 的中文任务描述
    task_prompt = f"""
    你是一个今日头条自动化发布助手。请严格执行以下步骤：
    1. 访问发布页面: {PUBLISH_URL}
    2. 页面加载后，如果跳转到了登录页 (URL 包含 login)，请报错停止。
    3. 检查页面上是否有任何弹窗、引导信息 (如“我知道了”、“不再提示”) 或侧边助手面板 (头条创作助手)。如果有，请先关闭它们，确保不会遮挡后续操作。
    4. 如果在发布页，找到标题录入框 (placeholder 包含“请输入文章标题”)，填入: "{title}"
    5. 找到正文编辑器 (ProseMirror)，点击它以获取焦点，然后填入内容: "{content}"
    6. 在“展示封面”中，点击选择“无封面”。
    7. 滚动到页面下方，找到“发文设置”区域。如果它是收起状态，请点击展开。
    8. 在发文设置中，勾选“头条首发”。
    9. 点击页面底部的“预览并发布”按钮 (class 为 publish-btn-last)。
    10. 等待确认发布成功的消息。
    11. 完成任务后，请汇报结果。成功则说“发布完成”，失败请描述详细阻碍。
    """

    browser = Browser(
        storage_state=STATE_FILE,
        headless=False
    )

    agent = Agent(
        task=task_prompt,
        llm=llm,
        browser=browser
    )

    print("正在启动 AI Agent 尝试发布文章...")
    try:
        history = await agent.run()
        # 打印最终结果
        result = history.final_result()
        if result:
            print(f"\nAI 任务反馈: {result}")
        else:
            print("\nAI 任务结束，未返回具体结果。")
    finally:
        await browser.stop()

async def main():
    parser = argparse.ArgumentParser(description="今日头条 AI 自动发布工具")
    parser.add_argument("--title", type=str, help="文章标题")
    parser.add_argument("--content", type=str, help="文章正文")
    parser.add_argument("--login", action="store_true", help="执行登录并保存状态")
    parser.add_argument("--model", type=str, help="指定 LLM 模型名称 (如 gpt-4o)")

    args = parser.parse_args()

    if args.login:
        await perform_login(args.model)
        return

    if not args.title or not args.content:
        print("提示: 必须提供 --title 和 --content 参数。")
        print("或者使用 --login 仅初始化登录状态。")
        print("用法示例: python publisher_ai.py --title '我的AI文章' --content '这是正文内容...'")
        return

    await run_publisher(args.title, args.content, args.model)

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\n[!] 用户取消操作。")
    except Exception as e:
        print(f"\n[!] 运行出错: {e}")
        import traceback
        traceback.print_exc()

#!/usr/bin/env python3
"""
今日头条自动发布脚本
使用 Playwright 实现浏览器自动化发布
"""

import asyncio
import json
import sys
import os
from datetime import datetime
from pathlib import Path
from playwright.async_api import async_playwright


class ToutiaoPublisher:
    def __init__(self, config_path: str = None):
        self.config = self._load_config(config_path)
        self.browser = None
        self.context = None
        self.page = None
        self.playwright = None

    def _load_config(self, config_path: str = None):
        if config_path is None:
            config_path = os.path.join(os.path.dirname(__file__), "config.json")

        if os.path.exists(config_path):
            with open(config_path, 'r', encoding='utf-8') as f:
                return json.load(f)

        return {
            "headless": True,
            "timeout": 30000,
            "login": {
                "username": "",
                "password": ""
            }
        }

    async def init_browser(self):
        """初始化浏览器"""
        self.playwright = await async_playwright().start()
        self.browser = await self.playwright.chromium.launch(
            headless=self.config.get("headless", True)
        )
        self.context = await self.browser.new_context(
            viewport={"width": 1280, "height": 720},
            user_agent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        )
        self.page = await self.context.new_page()
        await self.page.set_default_timeout(self.config.get("timeout", 30000))

    async def close(self):
        """关闭浏览器"""
        if self.browser:
            await self.browser.close()
        if self.playwright:
            await self.playwright.stop()

    async def login(self) -> bool:
        """登录今日头条"""
        print("[ToutiaoPublisher] 正在打开登录页面...")

        login_url = "https://mp.toutiao.com/auth/page/login/"
        await self.page.goto(login_url)
        await self.page.wait_for_load_state("networkidle")

        login_info = self.config.get("login", {})
        username = login_info.get("username", "")
        password = login_info.get("password", "")

        if not username or not password:
            print("[ToutiaoPublisher] 请在配置文件中填写用户名和密码")
            return False

        try:
            # 输入用户名
            await self.page.fill('input[name="username"]', username)
            await asyncio.sleep(0.5)

            # 输入密码
            await self.page.fill('input[name="password"]', password)
            await asyncio.sleep(0.5)

            # 点击登录按钮
            await self.page.click('button[type="submit"]')
            await asyncio.sleep(2)

            # 检查是否登录成功
            current_url = self.page.url
            if "login" not in current_url:
                print("[ToutiaoPublisher] 登录成功!")
                return True
            else:
                print("[ToutiaoPublisher] 登录失败，请检查用户名和密码")
                return False

        except Exception as e:
            print(f"[ToutiaoPublisher] 登录过程出错: {e}")
            return False

    async def publish_article(self, title: str, content: str, category: str = "科技") -> dict:
        """发布文章"""
        print(f"[ToutiaoPublisher] 正在发布文章: {title}")

        try:
            # 跳转到发布页面
            publish_url = "https://mp.toutiao.com/profile_v4/graphic/publish"
            await self.page.goto(publish_url)
            await self.page.wait_for_load_state("networkidle")
            await asyncio.sleep(1)

            # 输入标题
            await self.page.fill('input[placeholder*="标题"]', title)
            await asyncio.sleep(0.5)

            # 点击内容区域并输入内容
            # 尝试多种可能的选择器
            content_selectors = [
                'div[contenteditable="true"]',
                'textarea[placeholder*="正文"]',
                'div[class*="editor"]',
                'div[class*="content"]'
            ]

            content_filled = False
            for selector in content_selectors:
                try:
                    await self.page.click(selector)
                    await self.page.fill(selector, content)
                    content_filled = True
                    break
                except:
                    continue

            if not content_filled:
                print(f"[ToutiaoPublisher] 无法找到内容输入区域")

            await asyncio.sleep(0.5)

            # 选择分类（如果界面有分类选项）
            try:
                category_button = await self.page.query_selector('span:has-text("选择分类")')
                if category_button:
                    await category_button.click()
                    await asyncio.sleep(0.5)
                    await self.page.click(f'text="{category}"')
            except:
                pass

            # 点击发布按钮
            publish_button_selectors = [
                'button:has-text("发布")',
                'button:has-text("发表")',
                'div[role="button"]:has-text("发布")'
            ]

            for selector in publish_button_selectors:
                try:
                    await self.page.click(selector)
                    break
                except:
                    continue

            await asyncio.sleep(2)

            # 获取发布结果
            current_url = self.page.url
            if "success" in current_url or "publish" in current_url:
                return {
                    "success": True,
                    "title": title,
                    "url": current_url,
                    "message": "发布成功"
                }
            else:
                return {
                    "success": False,
                    "title": title,
                    "message": "发布可能失败，请手动检查"
                }

        except Exception as e:
            print(f"[ToutiaoPublisher] 发布文章出错: {e}")
            return {
                "success": False,
                "title": title,
                "message": str(e)
            }

    async def publish_from_file(self, news_file: str) -> list:
        """从 JSON 文件读取新闻并发布"""
        print(f"[ToutiaoPublisher] 正在从文件读取新闻: {news_file}")

        if not os.path.exists(news_file):
            print(f"[ToutiaoPublisher] 文件不存在: {news_file}")
            return []

        with open(news_file, 'r', encoding='utf-8') as f:
            news_data = json.load(f)

        results = []

        # 登录
        if not await self.login():
            print("[ToutiaoPublisher] 登录失败，停止发布")
            return results

        # 获取新闻列表
        news_list = news_data.get("UniqueNews", [])
        if not news_list:
            news_list = news_data.get("unique_news", [])

        print(f"[ToutiaoPublisher] 找到 {len(news_list)} 条新闻")

        for i, news in enumerate(news_list):
            title = news.get("Title", news.get("title", ""))
            content = news.get("Content", news.get("content", ""))
            summary = news.get("Summary", news.get("summary", ""))

            if not content and summary:
                content = summary

            if title and content:
                # 构建完整内容
                full_content = f"{content}\n\n来源: {news.get('Source', '未知')}"
                result = await self.publish_article(title, full_content)
                results.append(result)
                print(f"[ToutiaoPublisher] [{i+1}/{len(news_list)}] {result}")
                await asyncio.sleep(2)  # 间隔2秒
            else:
                print(f"[ToutiaoPublisher] 新闻 {i+1} 缺少标题或内容，跳过")

        return results


async def main():
    """主函数"""
    print("=" * 50)
    print("今日头条自动发布工具")
    print("=" * 50)

    if len(sys.argv) > 1 and sys.argv[1] in ["-h", "--help", "help"]:
        print("用法: python toutiao_publisher.py <新闻文件路径>")
        print("示例: python toutiao_publisher.py news_publish_toutiao_20250101_120000.json")
        print("\n配置文件: scripts/config.json")
        print("请先填写 config.json 中的登录信息")
        return

    news_file = None
    if len(sys.argv) > 1:
        news_file = sys.argv[1]
    else:
        output_dir = os.path.join(os.path.dirname(__file__), "..")
        json_files = list(Path(output_dir).glob("news_publish_*.json"))
        if json_files:
            json_files.sort(key=lambda x: x.stat().st_mtime, reverse=True)
            news_file = str(json_files[0])
            print(f"使用最新的新闻文件: {news_file}")

    if not news_file:
        print("用法: python toutiao_publisher.py <新闻文件路径>")
        print("示例: python toutiao_publisher.py news_publish_toutiao_20250101_120000.json")
        sys.exit(1)

    publisher = ToutiaoPublisher()

    try:
        await publisher.init_browser()
        results = await publisher.publish_from_file(news_file)

        print("\n" + "=" * 50)
        print(f"发布完成，共处理 {len(results)} 篇文章")
        success_count = sum(1 for r in results if r.get("success"))
        print(f"成功: {success_count}, 失败: {len(results) - success_count}")
        print("=" * 50)

    finally:
        await publisher.close()


if __name__ == "__main__":
    asyncio.run(main())

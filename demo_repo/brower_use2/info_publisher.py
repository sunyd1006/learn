"""
info_publisher.py — CLI entry point for the modular publisher system.

Usage:
    uv run python info_publisher.py --platform toutiao \\
        --docx articles/file1.docx \\
        --title "My Article" \\
        [--force-login] \\
        [--password-login] \\
        [--headless]

Supported platforms:
    toutiao     — 今日头条 (implemented)
    xiaohongshu — 小红书 (stub, not yet implemented)
"""

from __future__ import annotations

import argparse
import logging
import os
import sys

from publishers import Article, ToutiaoPublisher, XiaohongshuPublisher
from publishers.base import BasePublisher

# ---------------------------------------------------------------------------
# Logging setup
# ---------------------------------------------------------------------------

LOG_DIR = os.path.join(os.getcwd(), "logs")
os.makedirs(LOG_DIR, exist_ok=True)

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s | %(levelname)-8s | %(name)s | %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
    handlers=[
        logging.StreamHandler(sys.stdout),
        logging.FileHandler(
            os.path.join(LOG_DIR, "publisher.log"),
            encoding="utf-8",
        ),
    ],
)

logger = logging.getLogger("info_publisher")

# ---------------------------------------------------------------------------
# Platform registry
# ---------------------------------------------------------------------------

PLATFORM_MAP: dict[str, type[BasePublisher]] = {
    "toutiao": ToutiaoPublisher,
    "xiaohongshu": XiaohongshuPublisher,
}


# ---------------------------------------------------------------------------
# CLI
# ---------------------------------------------------------------------------

def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        prog="info_publisher",
        description="Modular article publisher — supports Toutiao, Xiaohongshu, and more.",
    )
    parser.add_argument(
        "--platform",
        choices=list(PLATFORM_MAP),
        default="toutiao",
        help="Target platform (default: toutiao)",
    )
    parser.add_argument(
        "--docx",
        required=True,
        help="Path to the .docx article file to publish",
    )
    parser.add_argument(
        "--title",
        default="acb",
        help="Article title (default: 'acb')",
    )
    parser.add_argument(
        "--cover",
        default="无封面",
        choices=["无封面", "单图", "三图"],
        help="Cover type (default: 无封面)",
    )
    parser.add_argument(
        "--no-first-release",
        dest="first_release",
        action="store_false",
        help="Do NOT check 头条首发",
    )
    parser.add_argument(
        "--declarations",
        nargs="+",
        default=["取材网络", "引用AI"],
        help="Work declaration labels to check (default: 取材网络 引用AI)",
    )
    parser.add_argument(
        "--force-login",
        action="store_true",
        help="Ignore cached session and force a fresh login",
    )
    parser.add_argument(
        "--password-login",
        action="store_true",
        help="Use automated password login (reads TOUTIAO_USERNAME/PASSWORD from .env)",
    )
    parser.add_argument(
        "--headless",
        action="store_true",
        help="Run browser in headless mode",
    )
    return parser


def main() -> int:
    parser = build_parser()
    args = parser.parse_args()

    logger.info(
        "Starting publisher | platform=%s | docx=%s | title=%s",
        args.platform, args.docx, args.title,
    )

    PublisherClass = PLATFORM_MAP[args.platform]
    article = Article(
        title=args.title,
        docx_path=args.docx,
        cover=args.cover,
        first_release=args.first_release,
        declarations=args.declarations,
    )

    with PublisherClass(headless=args.headless) as publisher:
        try:
            publisher.login(
                force=args.force_login,
                use_password=args.password_login,
            )
            success = publisher.publish(article)
            if success:
                logger.info("✅ [PASS] 文章发布成功")
                return 0
            else:
                logger.error("❌ [FAIL] 文章发布失败")
                return 1
        except NotImplementedError as exc:
            logger.error("Platform not yet implemented: %s", exc)
            return 2
        except Exception as exc:
            logger.exception("Unexpected error during publish: %s", exc)
            return 1


if __name__ == "__main__":
    sys.exit(main())

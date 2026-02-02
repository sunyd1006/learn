"""
publishers/xiaohongshu.py — XiaohongshuPublisher (stub)

Placeholder for future Xiaohongshu (小红书) publishing support.
Raises NotImplementedError until implemented.
"""

from __future__ import annotations

from publishers.base import Article, BasePublisher


class XiaohongshuPublisher(BasePublisher):
    """Publishing stub for 小红书 — not yet implemented."""

    PLATFORM = "xiaohongshu"

    def login(self, force: bool = False, use_password: bool = False) -> None:
        raise NotImplementedError(
            "XiaohongshuPublisher.login() is not yet implemented. "
            "Contributions welcome!"
        )

    def publish(self, article: Article) -> bool:
        raise NotImplementedError(
            "XiaohongshuPublisher.publish() is not yet implemented."
        )

    def close(self) -> None:
        pass  # Nothing to release

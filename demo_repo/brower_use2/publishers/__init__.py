"""
publishers package — interface-oriented article publishing system.

Supported platforms:
  - toutiao (ToutiaoPublisher)
  - xiaohongshu (XiaohongshuPublisher) [stub]
"""

from publishers.base import BasePublisher, Article
from publishers.toutiao import ToutiaoPublisher
from publishers.xiaohongshu import XiaohongshuPublisher

__all__ = ["BasePublisher", "Article", "ToutiaoPublisher", "XiaohongshuPublisher"]

"""
publishers/base.py — Abstract base class defining the Publisher interface.

All platform-specific publishers must subclass BasePublisher and implement
the abstract methods: login(), publish(), close().
"""

from __future__ import annotations

import logging
import os
from abc import ABC, abstractmethod
from dataclasses import dataclass, field
from typing import Optional


@dataclass
class Article:
    """Structured article payload passed to Publisher.publish()."""

    title: str
    docx_path: str                          # Absolute or CWD-relative path to .docx file
    cover: str = "无封面"                   # "无封面" | "单图" | "三图"
    first_release: bool = True              # Check 头条首发
    declarations: list[str] = field(
        default_factory=lambda: ["取材网络", "引用AI"]
    )                                       # Work declaration checkboxes to select

    def resolve_docx(self) -> str:
        """Return absolute path to the docx file, raising if not found."""
        path = os.path.abspath(self.docx_path)
        if not os.path.exists(path):
            raise FileNotFoundError(f"Article docx not found: {path}")
        return path


class BasePublisher(ABC):
    """
    Abstract publisher interface.

    Subclasses implement platform-specific login and publish logic.

    Usage:
        publisher = ToutiaoPublisher(headless=False)
        try:
            publisher.login(use_password=True)
            success = publisher.publish(article)
        finally:
            publisher.close()
    """

    #: Platform identifier — set by each subclass (e.g. "toutiao", "xiaohongshu")
    PLATFORM: str = ""

    def __init__(self, headless: bool = False):
        self.headless = headless
        self.logger = logging.getLogger(f"publisher.{self.PLATFORM}")

    # ------------------------------------------------------------------
    # Abstract interface
    # ------------------------------------------------------------------

    @abstractmethod
    def login(
        self,
        force: bool = False,
        use_password: bool = False,
    ) -> None:
        """
        Ensure the publisher is logged in.

        Args:
            force: If True, skip cached session and always do a fresh login.
            use_password: If True and credentials are available in env,
                          attempt automated password-based login.
                          Otherwise falls back to manual (QR / interactive).
        """

    @abstractmethod
    def publish(self, article: Article) -> bool:
        """
        Publish the given article.

        Returns:
            True on success, False on failure.
        """

    @abstractmethod
    def close(self) -> None:
        """Release browser resources."""

    # ------------------------------------------------------------------
    # Convenience: context-manager support
    # ------------------------------------------------------------------

    def __enter__(self) -> "BasePublisher":
        return self

    def __exit__(self, *_) -> None:
        self.close()

    # ------------------------------------------------------------------
    # Shared helpers
    # ------------------------------------------------------------------

    @property
    def state_path(self) -> str:
        """Path to the saved Playwright storage-state file for this platform."""
        return os.path.join(os.getcwd(), "auth", f"{self.PLATFORM}_state.json")

    def _has_cached_state(self) -> bool:
        return os.path.exists(self.state_path)

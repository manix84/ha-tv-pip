"""Repair issue helpers for HA TV PiP."""

from __future__ import annotations

import importlib
from typing import Any

from .const import CONF_NAME, DOMAIN

VERSION_MISMATCH_ISSUE_PREFIX = "receiver_version_mismatch_"


def async_update_version_alignment_issue(
    hass: Any,
    entry: Any,
    alignment: dict[str, str | bool],
) -> None:
    """Create or clear a repair issue for receiver/integration version drift."""

    if hass is None:
        return

    issue_registry = _issue_registry()
    if issue_registry is None:
        return

    issue_id = f"{VERSION_MISMATCH_ISSUE_PREFIX}{entry.entry_id}"
    if alignment.get("version_alignment") != "mismatch":
        issue_registry.async_delete_issue(hass, DOMAIN, issue_id)
        return

    issue_registry.async_create_issue(
        hass,
        DOMAIN,
        issue_id,
        is_fixable=False,
        severity=issue_registry.IssueSeverity.WARNING,
        translation_key="receiver_version_mismatch",
        translation_placeholders={
            "receiver": str(entry.data.get(CONF_NAME, "HA TV PiP Receiver")),
            "receiver_version": str(alignment.get("receiver_version", "unknown")),
            "integration_version": str(
                alignment.get("integration_version", "unknown")
            ),
        },
    )


def _issue_registry() -> Any | None:
    try:
        return importlib.import_module("homeassistant.helpers.issue_registry")
    except ModuleNotFoundError:
        return None

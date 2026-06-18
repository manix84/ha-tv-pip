"""Restreaming provider metadata for HA TV PiP."""

from __future__ import annotations

from typing import Any

RESTREAMING_PROVIDER_STATUS = "planned"

RESTREAMING_PROVIDER_METADATA: dict[str, Any] = {
    "enabled": False,
    "status": RESTREAMING_PROVIDER_STATUS,
    "configured_provider": None,
    "active_provider": None,
    "supported_providers": [],
    "planned_providers": ["go2rtc", "webrtc", "transcoding"],
}


def restreaming_provider_metadata() -> dict[str, Any]:
    """Return a copy of the current restreaming provider metadata."""

    return dict(RESTREAMING_PROVIDER_METADATA)

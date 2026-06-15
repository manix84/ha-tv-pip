"""Tests for config-flow helpers."""

# ruff: noqa: E402, I001

import sys
import types
from dataclasses import dataclass
from typing import Any

homeassistant = types.ModuleType("homeassistant")
config_entries = types.ModuleType("homeassistant.config_entries")


class FakeConfigFlow:
    def __init_subclass__(cls, **kwargs: Any) -> None:
        super().__init_subclass__()


config_entries.ConfigFlow = FakeConfigFlow  # type: ignore[attr-defined]
homeassistant.config_entries = config_entries  # type: ignore[attr-defined]
sys.modules.setdefault("homeassistant", homeassistant)
sys.modules.setdefault("homeassistant.config_entries", config_entries)

from custom_components.ha_tv_pip.config_flow import _receiver_from_zeroconf  # noqa: E402


@dataclass
class FakeZeroconfDiscoveryInfo:
    host: str
    port: int | None
    properties: dict[str, Any]


def test_receiver_from_zeroconf_uses_duck_typed_discovery_info() -> None:
    receiver = _receiver_from_zeroconf(
        FakeZeroconfDiscoveryInfo(
            host="10.0.0.236",
            port=8765,
            properties={
                "id": b"49e3b07d8f4b7d65",
                "name": b"Nursery TV",
                "version": b"0.8.2",
                "pairing": b"disabled",
                "api": b"1",
            },
        )
    )

    assert receiver.device_id == "49e3b07d8f4b7d65"
    assert receiver.name == "Nursery TV"
    assert receiver.host == "10.0.0.236"
    assert receiver.port == 8765

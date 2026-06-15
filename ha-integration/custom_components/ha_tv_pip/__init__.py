"""HA TV PiP Home Assistant integration.

The first integration slice supports Zeroconf discovery and config entry
creation. Device control, pairing, services, and entities are added in later
stages.
"""

from typing import Any

from .const import DOMAIN

__all__ = ["DOMAIN"]


async def async_setup_entry(hass: Any, entry: Any) -> bool:
    """Set up HA TV PiP from a config entry."""

    return True


async def async_unload_entry(hass: Any, entry: Any) -> bool:
    """Unload a HA TV PiP config entry."""

    return True

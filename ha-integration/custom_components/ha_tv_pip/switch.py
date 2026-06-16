"""Switch platform for HA TV PiP."""

from __future__ import annotations

from typing import TYPE_CHECKING, Any

from .client import (
    ReceiverClientError,
    async_get_receiver_status,
    async_set_launcher_visible,
)
from .const import CONF_TOKEN
from .entity import ReceiverEntity

if TYPE_CHECKING:

    class SwitchEntity:
        """Fallback base for unit tests outside Home Assistant."""

    class EntityCategory:
        """Fallback entity category for unit tests outside Home Assistant."""

        CONFIG = "config"


else:
    try:
        from homeassistant.components.switch import SwitchEntity
        from homeassistant.const import EntityCategory
    except ModuleNotFoundError:

        class SwitchEntity:
            """Fallback base for unit tests outside Home Assistant."""

        class EntityCategory:
            """Fallback entity category for unit tests outside Home Assistant."""

            CONFIG = "config"


async def async_setup_entry(hass: Any, entry: Any, async_add_entities: Any) -> None:
    """Set up HA TV PiP receiver switches."""

    async_add_entities([ReceiverLauncherSwitch(entry)])


class ReceiverLauncherSwitch(ReceiverEntity, SwitchEntity):
    """Switch that controls whether the receiver appears in the TV launcher."""

    def __init__(self, entry: Any) -> None:
        super().__init__(entry, key="hide_launcher", name="Hide Launcher")
        self._attr_entity_category = EntityCategory.CONFIG
        self._attr_is_on: bool | None = None
        self._attr_extra_state_attributes: dict[str, Any] = {
            "recovery_hint": (
                "If enabled, reopen HA TV PiP from the Open Launcher button or "
                "Android Settings > Apps."
            )
        }

    async def async_update(self) -> None:
        """Poll launcher visibility from receiver status."""

        try:
            status = await async_get_receiver_status(self.host, self.port)
        except ReceiverClientError as error:
            self._attr_is_on = None
            self._attr_extra_state_attributes = {"last_error": str(error)}
            return

        self._attr_is_on = (
            None if status.launcher_visible is None else not status.launcher_visible
        )
        self._attr_extra_state_attributes = {
            "recovery_hint": (
                "If enabled, reopen HA TV PiP from the Open Launcher button or "
                "Android Settings > Apps."
            )
        }

    async def async_turn_on(self, **kwargs: Any) -> None:
        """Hide the receiver from the TV launcher."""

        launcher_visible = await async_set_launcher_visible(
            self.host,
            self.port,
            token=str(self.entry.data[CONF_TOKEN]),
            visible=False,
        )
        self._attr_is_on = not launcher_visible

    async def async_turn_off(self, **kwargs: Any) -> None:
        """Show the receiver in the TV launcher."""

        launcher_visible = await async_set_launcher_visible(
            self.host,
            self.port,
            token=str(self.entry.data[CONF_TOKEN]),
            visible=True,
        )
        self._attr_is_on = not launcher_visible

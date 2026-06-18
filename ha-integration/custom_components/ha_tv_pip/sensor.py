"""Sensor platform for HA TV PiP."""

from __future__ import annotations

from datetime import datetime
from typing import TYPE_CHECKING, Any

from .client import ReceiverClientError, ReceiverStatus, async_get_receiver_status
from .const import DOMAIN
from .entity import ReceiverEntity
from .services import CAMERA_COMPATIBILITY_KEY, CAMERA_LAST_RESULT_KEY

if TYPE_CHECKING:

    class SensorEntity:
        """Fallback base for unit tests outside Home Assistant."""


else:
    try:
        from homeassistant.components.sensor import SensorEntity
    except ModuleNotFoundError:

        class SensorEntity:
            """Fallback base for unit tests outside Home Assistant."""


async def async_setup_entry(hass: Any, entry: Any, async_add_entities: Any) -> None:
    """Set up HA TV PiP receiver sensors."""

    async_add_entities(
        [
            ReceiverStatusSensor(entry),
            ReceiverDisplayModeSensor(entry),
            ReceiverStreamTypeSensor(entry),
            ReceiverLastErrorSensor(entry),
            ReceiverVersionSensor(entry),
            ReceiverLastCameraCompatibilitySensor(hass, entry),
            ReceiverLastCameraResultSensor(hass, entry),
        ]
    )


class ReceiverPollingSensor(ReceiverEntity, SensorEntity):
    """Base sensor that polls the receiver status endpoint."""

    unavailable_value = "unavailable"

    def __init__(self, entry: Any, *, key: str, name: str) -> None:
        super().__init__(entry, key=key, name=name)
        self._attr_native_value: str | None = self.unavailable_value
        self._attr_extra_state_attributes: dict[str, Any] = {}

    async def async_update(self) -> None:
        """Poll the receiver status endpoint."""

        try:
            status = await async_get_receiver_status(self.host, self.port)
        except ReceiverClientError as error:
            self._attr_native_value = "unavailable"
            self._attr_extra_state_attributes = {
                "connected": False,
                "last_error": str(error),
                "last_seen": None,
            }
            return

        self._attr_native_value = self._native_value(status)
        self._attr_extra_state_attributes = self._extra_attributes(status)

    def _native_value(self, status: ReceiverStatus) -> str | None:
        raise NotImplementedError

    def _extra_attributes(self, status: ReceiverStatus) -> dict[str, Any]:
        return _status_attributes(status)


class ReceiverStatusSensor(ReceiverPollingSensor):
    """Receiver playback/status sensor."""

    def __init__(self, entry: Any) -> None:
        super().__init__(entry, key="status", name="Status")

    def _native_value(self, status: ReceiverStatus) -> str:
        return status.playback_state


class ReceiverDisplayModeSensor(ReceiverPollingSensor):
    """Receiver display mode sensor."""

    def __init__(self, entry: Any) -> None:
        super().__init__(entry, key="display_mode", name="Active Display Mode")

    def _native_value(self, status: ReceiverStatus) -> str:
        return status.display_mode


class ReceiverStreamTypeSensor(ReceiverPollingSensor):
    """Receiver active stream type sensor."""

    unavailable_value = "unknown"

    def __init__(self, entry: Any) -> None:
        super().__init__(entry, key="stream_type", name="Active Stream Type")

    def _native_value(self, status: ReceiverStatus) -> str:
        return status.stream_type or "unknown"


class ReceiverLastErrorSensor(ReceiverPollingSensor):
    """Receiver last error sensor."""

    def __init__(self, entry: Any) -> None:
        super().__init__(entry, key="last_error", name="Last Receiver Error")

    def _native_value(self, status: ReceiverStatus) -> str:
        return status.error or "none"


class ReceiverVersionSensor(ReceiverPollingSensor):
    """Receiver app version sensor."""

    def __init__(self, entry: Any) -> None:
        super().__init__(entry, key="receiver_version", name="Receiver Version")

    def _native_value(self, status: ReceiverStatus) -> str:
        return status.version or "unknown"


class ReceiverLastCameraResultSensor(ReceiverEntity, SensorEntity):
    """Last camera command result stored by the Home Assistant integration."""

    def __init__(self, hass: Any, entry: Any) -> None:
        super().__init__(entry, key="last_camera_result", name="Last Camera Result")
        self.hass = hass
        self._attr_native_value: str = "none"
        self._attr_extra_state_attributes: dict[str, Any] = {}

    async def async_update(self) -> None:
        """Refresh the last stored camera action result."""

        result = (
            getattr(self.hass, "data", {})
            .get(DOMAIN, {})
            .get(CAMERA_LAST_RESULT_KEY, {})
            .get(self.entry.entry_id, {})
        )
        if not isinstance(result, dict) or not result:
            self._attr_native_value = "none"
            self._attr_extra_state_attributes = {}
            return

        self._attr_native_value = str(result.get("status", "unknown"))
        self._attr_extra_state_attributes = dict(result)


class ReceiverLastCameraCompatibilitySensor(ReceiverEntity, SensorEntity):
    """Latest camera compatibility test result stored by the integration."""

    def __init__(self, hass: Any, entry: Any) -> None:
        super().__init__(
            entry,
            key="last_camera_compatibility",
            name="Last Camera Compatibility",
        )
        self.hass = hass
        self._attr_native_value: str = "none"
        self._attr_extra_state_attributes: dict[str, Any] = {}

    async def async_update(self) -> None:
        """Refresh the last stored camera compatibility result."""

        result = _latest_camera_compatibility_result(self.hass, self.entry.entry_id)
        if not result:
            self._attr_native_value = "none"
            self._attr_extra_state_attributes = {}
            return

        self._attr_native_value = str(result.get("recommended_stream_type", "none"))
        self._attr_extra_state_attributes = result


def _latest_camera_compatibility_result(
    hass: Any,
    entry_id: str,
) -> dict[str, Any]:
    receiver_results = (
        getattr(hass, "data", {})
        .get(DOMAIN, {})
        .get(CAMERA_COMPATIBILITY_KEY, {})
        .get(entry_id, {})
    )
    if not isinstance(receiver_results, dict):
        return {}

    results: list[dict[str, Any]] = []
    for result in receiver_results.values():
        if isinstance(result, dict):
            results.append(dict(result))

    if not results:
        return {}

    latest = results[0]
    for result in results[1:]:
        if str(result.get("tested_at", "")) > str(latest.get("tested_at", "")):
            latest = result
    return latest


def _status_attributes(status: ReceiverStatus) -> dict[str, Any]:
    attributes = {
        "connected": True,
        "app_version": status.version,
        "api_version": status.api_version,
        "device_id": status.device_id,
        "device_name": status.device_name,
        "display_mode": status.display_mode,
        "stream_type": status.stream_type,
        "pairing_state": status.pairing_state,
        "remote_status": status.remote_status,
        "compatibility_state": status.compatibility.state,
        "compatible": status.compatibility.compatible,
        "missing_features": list(status.compatibility.missing_features),
        "compatibility_warnings": list(status.compatibility.warnings),
        "control_running": status.control_running,
        "last_request": status.last_request,
        "last_error": status.error,
        "last_seen": datetime.now().isoformat(timespec="seconds"),
    }
    capabilities = status.capabilities
    if capabilities is not None:
        attributes.update(
            {
                "capabilities_version": capabilities.capabilities_version,
                "supported_stream_types": list(capabilities.stream_types),
                "supported_positions": list(capabilities.positions),
                "supports_preview_image": capabilities.preview_image,
                "supports_playable_fallback": capabilities.playable_fallback,
                "supports_native_picture_in_picture": (
                    capabilities.native_picture_in_picture
                ),
                "supports_overlay_fallback": capabilities.overlay_fallback,
                "supports_styled_notifications": capabilities.styled_notifications,
                "supports_media_with_notification_text": (
                    capabilities.media_with_notification_text
                ),
                "supports_launcher_management": capabilities.launcher_management,
                "supports_local_pairing": capabilities.local_pairing,
                "supports_remote_receiver_settings": (
                    capabilities.remote_receiver_settings
                ),
            }
        )

    return attributes

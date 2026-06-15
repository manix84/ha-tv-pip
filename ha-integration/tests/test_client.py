"""Tests for the HA TV PiP receiver client helpers."""

from custom_components.ha_tv_pip.client import _error_message


def test_error_message_extracts_receiver_error() -> None:
    assert _error_message('{"error":"invalid_code"}') == "invalid_code"


def test_error_message_ignores_invalid_json() -> None:
    assert _error_message("not json") is None

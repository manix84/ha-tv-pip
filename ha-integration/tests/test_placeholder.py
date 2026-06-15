import asyncio

from custom_components.ha_tv_pip import async_setup_entry, async_unload_entry
from custom_components.ha_tv_pip.const import DOMAIN


def test_domain_matches_integration_slug() -> None:
    assert DOMAIN == "ha_tv_pip"


def test_config_entry_lifecycle_hooks_return_true() -> None:
    assert asyncio.run(async_setup_entry(hass=object(), entry=object())) is True
    assert asyncio.run(async_unload_entry(hass=object(), entry=object())) is True

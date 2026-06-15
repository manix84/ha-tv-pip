# Home Assistant Automation Examples ⚙️

Automation examples will become executable once Stage 5 adds the first Home Assistant service.

Planned Stage 5 shape:

```yaml
alias: Show front door on TV
trigger:
  - platform: state
    entity_id: binary_sensor.front_door_bell_visitor
    to: "on"
action:
  - service: ha_tv_pip.show_camera
    target:
      device_id: living_room_tv
    data:
      camera_entity: camera.front_door
      duration_seconds: 30
      enter_pip: true
```

This is a roadmap example until `ha_tv_pip.show_camera` is implemented.

# Privacy Policy 🔒

HA TV PiP is pre-release local-first software. It does not collect, sell, or share personal data with the project maintainers.

## Current Phase 🧪

The Android TV app can play a public test HLS stream, advertise itself on the local network, and accept paired local commands from Home Assistant. It does not use analytics, telemetry, cloud relay, or maintainer-operated services.

## Data Collection 📦

The project maintainers receive no app telemetry or usage data.

The Android app may write local Android log messages for development and debugging, including playback state, PiP state, pairing state, request paths, and errors. Pairing tokens are not logged. These logs stay on the Android device unless a developer manually exports them with Android tooling.

## Network Access 🌐

The Android app uses network access to load configured HLS streams, advertise and serve its local control endpoint on the LAN, and receive paired local commands.

The Home Assistant integration stores receiver host, port, device id, receiver name, version, pairing state, API version, and a local bearer token in Home Assistant config entry data. Camera entity support is planned for Stage 5.

## Future Phases 🚧

Future phases may add Home Assistant camera stream playback, snapshots, WebRTC, and remote receiver modes. Privacy behavior should be updated before those features are released.

The intended direction is local-first control with no cloud relay by default.

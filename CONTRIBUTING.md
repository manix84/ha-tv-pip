# Contributing 🛠️

Thanks for helping improve HA TV PiP.

## Current Focus 🎯

The project is currently moving from Stage 4 to Stage 5:

- Android TV receiver app.
- Local HTTP control.
- mDNS discovery.
- Home Assistant discovery and pairing flow.
- Next target: `ha_tv_pip.show_camera`.

Please keep changes aligned with the roadmap and avoid jumping ahead to snapshots, WebRTC, remote access, Play Store, or HACS distribution unless that work has been explicitly started.

## Development Setup 💻

Open the Android app from:

```sh
android-tv-app/
```

Build from the command line:

```sh
npm run install:all
npm run check
```

You need JDK 17 or newer and the Android SDK installed.

## Pull Requests 📬

Good pull requests should:

- Stay focused on one change.
- Include clear testing notes.
- Avoid unrelated formatting churn.
- Keep future-phase work behind documentation or placeholders until that phase starts.

## Style Notes ✨

- Kotlin first.
- Gradle Kotlin DSL.
- Keep architecture simple and easy to extend.
- Prefer local-first designs for future control and discovery work.
- Emoji are welcome in docs when they make the tone friendlier.

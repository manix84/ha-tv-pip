# What's New ✨

## 0.4.0 - Chromecast Overlay Validation 📺

Phase 1 is now validated on physical Chromecast with Google TV hardware.

- Added device compatibility detection for native PiP and overlay support 🧭
- Added a no-ADB floating overlay fallback for Google TV devices that reject native PiP 🪟
- Added an overlay permission entry point from the Android TV app UI 🔐
- Added overlay stop handling from the receiver main screen 🛑
- Updated the player action label so fallback devices show `Show Overlay` instead of `Enter PiP` 🎮
- Confirmed the fallback works on Chromecast with Google TV after granting overlay permission ✅

## 0.3.0 - Phase 1 Complete ✅

Phase 1 is now complete and validated as the Android TV PiP MVP.

- Confirmed Android TV Kotlin receiver app builds successfully 📺
- Confirmed Media3 / ExoPlayer public HLS playback path compiles cleanly 🎬
- Confirmed manual and Home-triggered PiP support is implemented 🪟
- Added Android, Home Assistant integration, and website test coverage 🧪
- Added split GitHub Actions quality jobs for linting, type checking, tests, and dry-run builds ✅
- Added native Git pre-commit checks and version bump automation 🪝
- Added local dependency installer with Android SDK detection and Home Assistant Python virtualenv setup 🛠️
- Added GitHub Pages-ready promotional website build 🌐
- Updated Android SDK/build tools configuration for Android Studio 2026.1.1 / SDK 36.1 compatibility 🤖

## 0.1.0 - Phase 1 MVP 🚀

Initial Android TV Picture-in-Picture proof of concept.

- Added Android TV Kotlin app 📺
- Added Media3 / ExoPlayer HLS playback 🎬
- Added public test stream playback 🧪
- Added manual `Enter PiP` control 🪟
- Added automatic PiP entry when Home is pressed 🏠
- Added structured Android logging 🪵
- Added root monorepo layout for future Android app and Home Assistant integration work 🧱
- Added placeholder Home Assistant integration directory 🚧
- Added package metadata and version consistency checks 📌
- Added GitHub Release packaging for Android APK and Home Assistant integration zip 📦
- Added pre-commit semantic version bump automation 🔢
- Added promotional Vite website scaffold 🌐

## Not Included Yet 🛑

- Home Assistant integration
- Camera support
- Pairing
- Discovery
- Authentication
- Cloud connectivity
- Remote access
- WebRTC

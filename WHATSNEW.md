# What's New ✨

## 0.47.0 - Release Workflow Fix 📦

- Changed release automation to attach assets while the GitHub Release is still a draft so published immutable releases are not mutated 📦
- Documented that already-published failed releases must be deleted manually or replaced by bumping to the next version 📝

## 0.46.0 - Stage 12 Planning 📦

- Defined Stage 12 as beta release hardening instead of a new feature stage 🚧
- Added release-candidate goals for full checks, Android builds, HACS zip validation, website build validation, and release packaging ✅
- Updated root, Android, Home Assistant, website, examples, roadmap, development, Play Store, and official Home Assistant readiness docs 📝
- Updated the website locale status content so public pages point to Stage 12 and beta-readiness work 🌍
- Marked Tier 1 translations complete and Tier 2/Tier 3 translations planned across docs and the website 🌐

## 0.45.0 - Stage 11 Complete ✅

- Completed Stage 11 enhanced notifications after Chromecast manual testing 📺
- Added explicit receiver support for title-only media notification footers without requiring a message 🔔
- Kept resize-only camera and snapshot commands clean so width/height do not accidentally show a text footer 📐
- Stacked video or snapshot media above notification text inside one continuous rounded glass popup ✨
- Added translucent alpha-hex background support for glass-style notification overlays 🎨
- Added useful Home Assistant service field examples without forcing examples on receiver/entity selectors 🏠
- Expanded HA and Android tests for notification payloads, title-only footers, resize-only media, and service metadata 🧪

## 0.41.0 - Stage 11 Enhanced Notifications 🔔

- Started Stage 11 with `ha_tv_pip.show_notification` for styled TV text alerts 🏠
- Added Android receiver parsing for `streamType: notification` commands 📺
- Added Android overlay rendering for notification title, message, corner position, colors, text sizes, and duration 🎨
- Allowed camera and snapshot popups to include the same optional notification text and styling fields 📹
- Rounded the TV notification card corners so alert overlays feel less harsh 📺
- Added optional `width` and `height` fields for notification, camera, and snapshot overlays 📐
- Reused both local HTTP and remote WebSocket receiver transports for notification commands 🌍
- Added Home Assistant service metadata, translations, validation, and focused tests ✅

## 0.39.1 - Phase 10 Distribution Polish 🎨

- Started a TV-first receiver dashboard with summary cards, primary PiP controls, launcher controls, remote receiver settings, and diagnostics separated from everyday actions 📺
- Moved verbose endpoint, discovery, and compatibility details into a dedicated diagnostics section 🧰
- Improved D-pad startup focus by keeping `Play Test Video` in the first action group 🎮
- Added Home Assistant-assisted remote receiver config sync and made manual URL/token entry an advanced fallback 🔐
- Hid manual remote receiver fields behind an advanced setup action so normal TV setup stays cleaner 📺
- Added Tier 1 website locale content modules with browser language detection and manual language override 🌍
- Documented the Stage 10 decision to keep onboarding, pairing, and troubleshooting as dashboard sections instead of separate TV screens 🧭
- Added a TV-side pairing popup that prominently shows the active Home Assistant pairing code 📺
- Added glass-style dashboard cards with stronger TV focus treatment ✨
- Made dashboard sections selectable containers so D-pad navigation can move from section to controls and back more predictably 🎮
- Added confirmation warnings before `Reset Pairing` and `Clear Remote` run destructive actions ⚠️
- Completed manual Android TV D-pad testing on the receiver dashboard ✅
- Added root `hacs.json` for HACS zip-release installation 🧩
- Added a stable `ha-tv-pip-integration.zip` release asset for HACS while keeping the versioned integration zip 📦
- Added HACS custom-repository install docs 📝
- Changed release automation to run on pushes to `main`, creating or updating the versioned GitHub Release with Android and HACS assets 🚀
- Added Play Store release-prep materials covering listing copy, privacy wording, screenshots, signing, and release notes 📺
- Added Android release App Bundle build scripts for Play Store prep 📦
- Added official Home Assistant integration readiness checklist 🏠
- Documented future enhanced-notification overlay options for position, title, message, colors, and text sizes 🔔

## 0.28.0 - Phase 9 Documentation And FAQ Polish 📚

- Marked Phase 9 remote receiver transport as complete in the roadmap ✅
- Added website FAQ content for setup worries, stream compatibility, remote mode, and translations ❓
- Added translation planning for Android, Home Assistant, and website language support 🌍
- Added extra remote transport fallback and runtime-state tests 🧪

## 0.27.0 - Phase 9 Remote Receiver Transport 🌍

- Added Home Assistant WebSocket registration for outbound remote receivers 🏠
- Added Android TV remote receiver client settings for Home Assistant external URL and long-lived access token 📺
- Sent remote `show_camera` and `show_snapshot` commands over the receiver's outbound WebSocket connection when connected 🔔
- Kept local HTTP control as the fallback when no remote receiver connection is active 🏠
- Kept HA TV PiP local-first: this is not a hosted HA TV PiP cloud service ✅

## 0.26.0 - Stage 8 Receiver Management Extension 🧰

- Added a Hide Launcher switch for Home Assistant 🏠
- Added an Open Launcher button so Home Assistant can reopen the app after the launcher icon is hidden 📺
- Added authenticated receiver management endpoints for opening the app and showing or hiding the launcher icon 🔐
- Added Android boot and package-replaced startup handling so the local receiver service starts without manually opening the app after restart 🔄

## 0.25.0 - Stage 8 Receiver Entities 🩺

- Added receiver status and connected entities for Home Assistant dashboards 🏠
- Added receiver test and close buttons for quick receiver management 🎛️
- Added diagnostics output with token and stream URL redaction 🔐
- Added typed receiver client helpers for `/status` and `/close` 📡
- Confirmed receiver entities appear correctly in Home Assistant ✅

## 0.24.0 - Stage 7 Stream Type Options 🎛️

- Added `stream_type: auto`, `stream_type: hls`, and `stream_type: snapshot` to `ha_tv_pip.show_camera` 🏠
- Made automatic mode prefer HLS and fall back to a snapshot command when Home Assistant cannot resolve a stream 🖼️
- Kept receiver snapshot previews visible when video playback fails after the stream URL is accepted 📺
- Logged the selected stream type before sending receiver commands 📺
- Confirmed the Chromecast fallback path shows a snapshot with a small fallback message instead of a black box ✅

## 0.23.0 - Snapshot Fallback Previews 🖼️

- Added optional entity-based snapshot fallback previews while video streams load 🏠
- Let `ha_tv_pip.show_camera` use `snapshot_camera_entity` for a separate preview camera entity 📷
- Updated the receiver protocol to accept `previewUrl` and replace the preview with live video when playback is ready 📺

## 0.22.0 - Stage 6 Snapshot Support 🖼️

- Added `ha_tv_pip.show_snapshot` to display camera snapshots from Home Assistant 🏠
- Added Android TV snapshot overlay rendering for `streamType: snapshot` 📺
- Reused pairing, bearer-token auth, receiver targeting, and duration timeouts for snapshots 🔐
- Confirmed camera feeds and snapshot feeds both work on the Chromecast test receiver ✅

## 0.21.2 - Generic Stream Error Wording 🩺

- Generalised unsupported-stream messages so they are not tied to one camera brand or setup 📺
- Updated docs to describe codec/profile compatibility in vendor-neutral terms 🧭

## 0.21.1 - Stage 5 Stream Compatibility Polish 🎞️

- Added receiver-side decoder fallback before reporting unsupported stream failures 📺
- Added clearer TV overlay feedback for unsupported camera streams 🩺
- Documented that broad main-stream support needs future stream selection, restreaming, or transcoding 🧭

## 0.21.0 - Stage 5 Service MVP 📹

- Added the first `ha_tv_pip.show_camera` service implementation 🏠
- Resolves Home Assistant camera HLS stream URLs and sends them to paired receivers 🎬
- Added service schema metadata for the Home Assistant UI 🧭
- Added focused service and receiver-client tests 🧪
- Verified the service path with a compatible lower-resolution camera stream on Chromecast 📺
- Added receiver playback diagnostics for unsupported stream codec errors 🩺
- Enabled Media3 decoder fallback for receiver playback before reporting unsupported codec failures 🎞️

## 0.18.0 - Stage 4 Complete 🔐

- Completed local pairing hardening for the Android TV receiver ✅
- Prevented remote clients from replacing an existing pairing without TV-side reset 🛡️
- Refreshed mDNS discovery metadata when pairing state changes 🔎
- Documented the Stage 4 pairing flow and reset process 📝
- Excluded `.DS_Store` files from packaged integration zips 📦

## 0.17.1 - Brand Icons And Pairing Polish 🎨

- Added Home Assistant custom integration brand images under `custom_components/ha_tv_pip/brand/` 🏠
- Confirmed Home Assistant shows the integration icon after restart 🧩
- Kept the direct integration `icon.png` and `logo.png` assets for compatibility 📁

## 0.17.0 - Local Receiver Pairing 🤝

- Added `/pair/start` and `/pair/confirm` endpoints to the Android TV receiver 🔐
- Showed the pairing code on the TV instead of exposing it in normal setup UX 📺
- Required bearer-token auth for `/show` and `/close` once pairing is required or complete 🛡️
- Added Home Assistant config-flow pairing and token storage 🏠
- Added a TV-side `Reset Pairing` action 🔁

## 0.14.1 - Confirmation Modal Polish 🧭

- Added a visible receiver-name field to the discovery confirmation modal so it is no longer blank 📝
- Kept the discovered receiver name as the default confirmation value 📺

## 0.14.0 - Device Page And Shared Icon 🧩

- Registered discovered receivers in Home Assistant's device registry so they have a device page 🏠
- Added explicit confirmation form schema and English translations for the discovery confirmation screen 📝
- Added a shared PNG project icon for Android, Home Assistant, and the website 🎨
- Lowered successful Zeroconf flow diagnostics back to debug logging after discovery validation 🪵

## 0.12.0 - Discovery Confirmation Flow 🔎

- Changed Zeroconf discovery to show a confirmation card instead of auto-creating entries 🧭
- Added confirmation text for discovered receivers 📝
- Kept manual host/port setup as a fallback path only 🛟

## 0.11.0 - Discovery Diagnostics 🔎

- Switched the Home Assistant Zeroconf manifest matcher to the explicit object format 🧭
- Added temporary warning-level Zeroconf flow diagnostics so HA logs show whether discovery reaches the integration 🪵
- Kept manual receiver setup as a fallback, not the primary path 🛟

## 0.10.0 - Manual Receiver Setup 🧭

- Added manual Home Assistant setup for receiver host and port 🏠
- Kept Zeroconf discovery as the preferred setup path 🔎
- Added manual setup validation and tests for fallback receiver entries 🧪

## 0.9.0 - Config Flow Load Fix 🧭

- Added a manual setup step that cleanly explains discovery-only setup 🏠
- Removed the runtime dependency on Home Assistant's Zeroconf type location 🔧
- Added config-flow helper coverage and Home Assistant strings for early abort reasons 🧪

## 0.8.1 - Home Assistant Entry Lifecycle 🧩

The Home Assistant scaffold can now load and unload discovered config entries cleanly.

- Added initial `async_setup_entry` and `async_unload_entry` lifecycle hooks 🏠
- Added async tests for config entry setup and unload behavior 🧪
- Kept receiver clients, entities, services, pairing, and authentication out of scope for this slice 🔒

## 0.8.0 - Home Assistant Discovery Scaffold 🏠

Stage 3 now has the first Home Assistant-side discovery scaffold.

- Added Home Assistant integration manifest with version sync and Zeroconf matching 🔎
- Added a config-flow entry point for HA TV PiP receiver discovery 🧭
- Added typed discovery payload parsing for receiver id, host, port, name, version, pairing state, and API version 🪪
- Added tests for Android TXT-record parsing and fallback defaults 🧪
- Confirmed the release zip now packages the integration files under `custom_components/ha_tv_pip/` 📦
- Kept TV control from Home Assistant, pairing, and authentication out of scope for this slice 🔒

## 0.7.0 - Stage 3 Discovery Begins 🔎

Stage 3 starts with Android-side local network discovery advertising.

- Added Android NSD / mDNS advertisement for `_ha-tv-pip._tcp.local.` 📡
- Added discovery metadata for device id, receiver name, app version, pairing state, and API version 🪪
- Added discovery state to `GET /status` responses 🩺
- Added discovery status to the Android TV main screen 📺
- Added unit coverage for discovery descriptors and runtime state 🧪
- Kept Home Assistant Zeroconf config flow, pairing, and authentication out of scope for this slice 🔒

## 0.6.0 - Stage 2 Endpoint Hardening 🛠️

The local control endpoint now handles the first round of real-device hardening after Chromecast validation.

- Enforced `durationSeconds` for the overlay fallback path ⏱️
- Added duplicate `/show` replacement feedback in API responses 🔁
- Added local endpoint address display on the Android TV main screen 📍
- Added `apiVersion` and `controlPort` to `/status` responses 📡
- Added endpoint uptime, request count, and previous request diagnostics to `/status` 🩺
- Added live endpoint diagnostics to the Android TV main screen 📺
- Added `GET /` API metadata plus JSON `404` and `405` error responses 🧭
- Added defensive local IP detection and endpoint display tests 🧪

## 0.5.0 - Stage 2 Local Control Begins 🌐

Stage 2 has started with a developer-testable local HTTP endpoint in the Android TV app.

- Added local receiver service on port `8765` 🛰️
- Added `GET /status` for version, device, playback, and display-mode state 📡
- Added `POST /show` for HLS playback commands 🎬
- Added `POST /close` for stopping playback and overlay fallback 🛑
- Added duplicate `/show` replacement feedback and overlay auto-close timing ⏱️
- Added the local endpoint address to the Android TV main screen 📍
- Added command validation and JVM unit tests for show requests 🧪
- Kept pairing, authentication, discovery, and Home Assistant integration out of scope for now 🔒

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

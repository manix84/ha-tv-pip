# Fire TV And Vega OS Release Prep

This document tracks Amazon Fire TV distribution options for HA TV PiP.

## Current Position

HA TV PiP currently ships an Android TV receiver:

```txt
com.hatvpip.receiver
```

The app requires a TV launcher surface through:

```xml
<uses-feature android:name="android.software.leanback" android:required="true" />
```

Amazon's Fire OS documentation describes Fire TV as an AOSP-based platform with Android app compatibility. That makes Fire OS the closest non-Google TV platform to the current Kotlin Android receiver.

Vega OS is a separate track. Do not assume the Android APK can run on Vega OS devices until Amazon's current developer tooling and submission path prove that.

## Distribution Paths

Amazon has two relevant paths:

- Amazon Appstore for Fire OS: likely target for the existing Android receiver, subject to Fire TV testing and Appstore review.
- Vega OS: research target for newer Fire TV OS devices; likely not compatible with the current APK/AAB as-is.

## Fire OS / Amazon Appstore

Fire OS is the practical first Amazon target because Fire TV supports Android apps and existing Android development tools.

### Artifact

Prefer the existing signed Android release APK for the first manual Appstore submission:

```txt
ha-tv-pip-android-release-vX.Y.Z.apk
```

Amazon Appstore also supports Android App Bundles, but the App Submission API does not support app bundles at this time. Because HA TV PiP already produces a release APK for GitHub/F-Droid and an AAB for Play, start with the APK unless Appstore testing shows the AAB path is materially better.

Do not create a Fire TV-specific APK unless Amazon review or Fire TV testing proves a real packaging, manifest, or compatibility difference is required.

### Expected Fit

Good candidates for reuse:

- D-pad-first dashboard and setup flow.
- Local network receiver model.
- No Google Play services dependency.
- Existing signed release APK.
- Existing Play/F-Droid listing copy and screenshots as a starting point.

High-risk areas to test on physical Fire TV hardware:

- `SYSTEM_ALERT_WINDOW` overlay behavior and user permission flow.
- Foreground service behavior for the local receiver.
- Boot/package-replaced startup handling.
- `LEANBACK_LAUNCHER` visibility and launcher hiding/recovery behavior.
- Picture-in-Picture support or fallback overlay behavior.
- HLS/MJPEG playback through ExoPlayer on Fire OS devices.
- Local network discovery and inbound HTTP control from Home Assistant.
- Remote receiver WebSocket mode when inbound LAN access is blocked.

### Listing Reuse

Start from:

- `docs/play-store.md`
- `docs/play-console-submission.md`
- `docs/play-store-data-safety.md`
- `docs/play-store-assets.md`
- `android-tv-app/fastlane/metadata/android/en-US/`

Expected listing values:

- App name: `HA TV PiP Receiver`
- Package name: `com.hatvpip.receiver`
- Category: smart home, tools, utilities, or video utility, depending on Appstore options.
- Privacy URL: `https://manix84.github.io/ha-tv-pip/privacy/`
- Data collection: no developer collection, no analytics, no ads, no telemetry.
- Pricing: free.
- DRM/IAP: disabled unless Amazon requires a store-specific selection.

### Submission Checklist

- Create or confirm Amazon Developer Console access.
- Confirm account identity verification is complete.
- Confirm whether Amazon Appstore accepts the current `com.hatvpip.receiver` package for Fire TV targeting.
- Confirm Appstore device filtering shows Fire TV devices as compatible with the current manifest.
- Confirm whether Amazon requires APK or whether AAB is preferred for this app.
- Upload the signed release APK from the latest GitHub Release.
- Confirm `versionCode` is higher than any prior Amazon upload.
- Reuse listing copy from Play/F-Droid, adjusting only for Amazon Appstore field names.
- Upload Fire TV screenshots or TV-safe screenshots from `docs/play-store-assets.md` where dimensions fit.
- Complete Amazon privacy labels using the Play Store data-safety answers as the source.
- Add reviewer notes explaining Home Assistant pairing, local network behavior, overlay permission, foreground service use, and no analytics/ads.
- Run Live App Testing before public release.

### Manual Device Test

Test on at least one physical Fire TV device before Appstore submission:

1. Install the signed release APK.
2. Open the receiver dashboard from the Fire TV launcher.
3. Pair from Home Assistant.
4. Send a styled notification.
5. Send a snapshot popup.
6. Send an HLS camera stream.
7. Send an MJPEG camera stream if available.
8. Confirm close/refresh/status controls work.
9. Reboot the Fire TV and confirm receiver recovery behavior.
10. Reset pairing and confirm launcher recovery behavior.

Record device model, Fire OS version, app version, receiver capabilities, overlay permission behavior, PiP behavior, and stream compatibility results.

## Vega OS

Vega OS should be treated as a new platform investigation, not as a normal Amazon Appstore upload.

Open questions:

- Whether Amazon exposes a public Vega OS SDK and submission path for non-partner apps.
- Whether Vega OS supports local receiver apps, or only hosted/cloud-streamed app surfaces.
- Whether Vega OS supports local network server endpoints, WebSocket clients, or both.
- Whether a Vega app can react to Home Assistant commands while backgrounded.
- Whether overlays, PiP, or video-window APIs exist for notification-style camera popups.
- Whether camera streams and snapshots from Home Assistant can be rendered with acceptable latency.
- Whether the Amazon Appstore listing can target Fire OS and Vega OS from one product, or needs separate app entries.

Likely implementation options:

- Native Vega/React Native receiver if Amazon exposes the necessary APIs.
- Web receiver if a web app can keep a reliable Home Assistant connection and render video.
- Remote-only receiver mode if inbound local HTTP is unavailable but outbound WebSocket is reliable.
- No Vega support until Amazon provides an app model that can support a local smart-home popup receiver.

Do not promise Vega OS support until a prototype proves the receiver lifecycle, local network, and video presentation model.

## Automation Policy

Until Fire OS manual submission succeeds:

- Do not add Amazon Appstore upload automation.
- Do not add Amazon-specific version codes.
- Do not create a Fire-specific APK.
- Do not fork the Android package name.

After one successful manual Appstore release, evaluate Amazon's App Submission API for APK updates. Keep AAB automation separate unless Amazon's API supports the chosen artifact type.

Vega OS automation is out of scope until Amazon's tooling and app model are proven.

## References

- Fire TV getting started: `https://developer.amazon.com/docs/fire-tv/get-started-with-fire-tv.html`
- Amazon Appstore submission overview: `https://developer.amazon.com/docs/app-submission/understanding-submission.html`
- Amazon Appstore app bundles: `https://developer.amazon.com/docs/app-submission/app-bundles.html`
- Fire OS device filtering: `https://developer.amazon.com/docs/app-submission/device-filtering-and-compatibility.html`
- Amazon Developer Console: `https://developer.amazon.com/`

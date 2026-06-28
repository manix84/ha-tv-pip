# Samsung Store Distribution Prep

This document tracks Samsung distribution options for HA TV PiP.

## Current Position

HA TV PiP currently ships an Android TV receiver:

```txt
com.hatvpip.receiver
```

The app requires Android TV / Google TV support through:

```xml
<uses-feature android:name="android.software.leanback" android:required="true" />
```

That means the current APK/AAB is not a general Samsung phone or tablet app. Treat Samsung distribution as an investigation track, not a ready release path.

## Samsung Distribution Paths

Samsung has two relevant store surfaces:

- Galaxy Store: Samsung's Android app store for Galaxy devices, managed through Galaxy Store Seller Portal.
- TV Seller Office: Samsung's Smart TV submission portal for Samsung TV applications.

These are not interchangeable. Samsung Smart TVs run Samsung's TV platform and use the Smart TV / TV Seller Office toolchain, not the current Kotlin Android TV APK.

## Galaxy Store

Galaxy Store is the closest analogue to Google Play. Samsung documents it as a Samsung-exclusive app store where developers register apps and in-app items in Seller Portal, test them, and launch them in Galaxy Store.

### Fit For Current Android Receiver

Unknown until verified in Seller Portal.

The current receiver is Android TV-only. Before preparing a Galaxy Store release, confirm whether Seller Portal accepts Android TV / Leanback-only APKs and exposes Samsung Android TV or compatible target devices. If Galaxy Store only targets Galaxy phones, tablets, and watches for this app type, do not submit the current receiver there.

### Artifact

If Seller Portal accepts the current receiver, use the existing signed release APK:

```txt
ha-tv-pip-android-release-vX.Y.Z.apk
```

Do not create a Samsung-specific APK unless Seller Portal requires a material packaging or manifest difference. A separate artifact would increase release risk and signing/channel confusion.

The Play Store AAB remains Play-specific:

```txt
ha-tv-pip-android-release-vX.Y.Z.aab
```

### Listing Reuse

Start from the existing Play Store/F-Droid metadata:

- `docs/play-store.md`
- `docs/play-console-submission.md`
- `docs/play-store-data-safety.md`
- `docs/play-store-assets.md`
- `android-tv-app/fastlane/metadata/android/en-US/`

Expected listing values:

- App name: `HA TV PiP Receiver`
- Package name: `com.hatvpip.receiver`
- Category: smart home, tools, or video utility, depending on Seller Portal options.
- Privacy URL: `https://manix84.github.io/ha-tv-pip/privacy/`
- Data collection: no developer collection, no analytics, no ads, no telemetry.

### Galaxy Store Checklist

- Create or confirm Samsung Seller Portal account access.
- Confirm whether Seller Portal accepts Android TV / Leanback-only APKs.
- Confirm whether Galaxy Store supports the receiver's required permissions, especially overlay and foreground service permissions.
- Confirm whether Galaxy Store requires data safety information for this app.
- Confirm whether any China distribution documentation is needed; avoid China distribution until the requirements are understood.
- Confirm the signed release APK installs on any Samsung Android target device Seller Portal claims is supported.
- Reuse Play Store screenshots only if Samsung's required screenshot dimensions and device categories match.
- Do not enable paid distribution, IAP, subscriptions, or ads.

## Samsung Smart TV / TV Seller Office

Samsung Smart TV distribution is a separate product track. Samsung's Smart TV docs describe Tizen web application projects created with Tizen Studio, and the TV Seller Office page describes an application package, self-testing, a test environment, and promotion assets for Samsung Smart TV app submission.

The current Android receiver cannot be uploaded as-is to TV Seller Office.

### Likely Separate Receiver

A Samsung TV receiver would likely need a new app implementation, probably as a Tizen web application. It should reuse the Home Assistant protocol and pairing model where possible, but it cannot assume Android services, Android overlays, Android Picture-in-Picture APIs, Android foreground services, or APK/AAB packaging.

Key unknowns:

- Whether Samsung TV apps can run a local HTTP/WebSocket receiver endpoint.
- Whether the app can wake, foreground itself, or react to Home Assistant events while not actively open.
- Whether native PiP, overlay, or video-window APIs can approximate the Android receiver behavior.
- Whether camera stream formats used by Home Assistant are supported by Samsung TV media APIs.
- Whether TV Seller Office review allows a local smart-home receiver utility with no content catalogue.

### Tizen Investigation Checklist

- Install Tizen Studio and Samsung TV extensions.
- Create a minimal Samsung TV web app proof of concept.
- Test local network access to Home Assistant.
- Test media playback for Home Assistant stream and snapshot URLs.
- Test whether an app can receive commands when backgrounded or only while foregrounded.
- Test Samsung TV PiP or overlay APIs against the desired notification behavior.
- Document required TV privileges and certification constraints.
- Decide whether a Samsung TV receiver belongs in this repository or a separate package.

## Release Policy

Until Seller Portal compatibility is proven:

- Do not add Galaxy Store upload automation.
- Do not add Samsung-specific version codes.
- Do not fork the Android receiver package name.
- Do not create a special Samsung APK.

If Galaxy Store accepts the existing Android TV receiver, automate only after one successful manual submission proves the artifact, signing, listing fields, and review requirements.

If Samsung Smart TV support is pursued, treat it as a separate platform stage rather than a store-upload extension of the Android release workflow.

## References

- Galaxy Store developer overview: `https://developer.samsung.com/galaxy-store`
- Galaxy Store Seller Portal: `https://seller.samsungapps.com/`
- Samsung Smart TV docs: `https://developer.samsung.com/smarttv/develop/getting-started/creating-tv-applications.html`
- TV Seller Office overview: `https://developer.samsung.com/tv-seller-office`

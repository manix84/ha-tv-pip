# F-Droid Release Prep

This document tracks the F-Droid release path for the HA TV PiP Android TV receiver.

## Release Model

F-Droid main repository releases are not uploaded from this repository in the same way Play Console AABs are uploaded. F-Droid builds APKs from public source using metadata in `fdroiddata`.

Once HA TV PiP is accepted into `fdroiddata`, future releases can be automated by:

1. Bumping the app version and Android `versionCode`.
2. Updating the F-Droid changelog file for that `versionCode`.
3. Tagging the release as `vX.Y.Z`.
4. Letting F-Droid's updater detect the tag and open or apply the next build metadata update.

## Upstream Metadata

F-Droid reads Fastlane-style upstream metadata from:

```txt
android-tv-app/fastlane/metadata/android/en-US/
```

Current metadata includes:

- `title.txt`
- `short_description.txt`
- `full_description.txt`
- `images/icon.png`
- `images/featureGraphic.jpg`
- `images/tvBanner.png`
- `images/phoneScreenshots/`
- `images/tvScreenshots/`
- `changelogs/<versionCode>.txt`

F-Droid changelog files must be named after the Android `versionCode` and stay under 500 characters.

Generate or refresh the current changelog with:

```sh
npm run fdroid:changelog
```

Check release readiness with:

```sh
npm run fdroid:changelog:check
```

The GitHub release workflow runs the check before building release artifacts.

## Draft fdroiddata Metadata

Initial metadata is tracked in this repository at:

```txt
docs/fdroiddata/metadata/com.hatvpip.receiver.yml
```

Copy it into a fork of `fdroiddata` at:

```txt
metadata/com.hatvpip.receiver.yml
```

## Signing

Initial F-Droid submission uses the default F-Droid signing model. F-Droid will build the APK from source and sign it with F-Droid's key.

The `fdroiddata` metadata intentionally does not include `Binaries` or `AllowedAPKSigningKeys` yet. That keeps the first submission focused on proving F-Droid can build the app from source.

### Channel Switching

The Android package name remains:

```txt
com.hatvpip.receiver
```

GitHub/Play builds and F-Droid builds will have different signing keys. Android does not allow an installed app to be updated by an APK signed with a different key, even when the package name is the same.

Users switching between GitHub/Play and F-Droid builds must:

1. Uninstall the currently installed receiver app.
2. Install the receiver from the new channel.
3. Delete the stale receiver entry in Home Assistant.
4. Pair the receiver again from Home Assistant.

Uninstalling the Android receiver clears local pairing tokens and receiver settings. The Home Assistant integration may still have the old config entry until the user deletes and re-adds it.

### Future Developer-Signed Path

Developer-signed F-Droid publishing requires reproducible APK builds and extra `fdroiddata` signature metadata. Treat that as a later hardening pass after the first F-Droid build succeeds.

Future work for developer-signed reproducible builds:

1. Build a signed GitHub release APK from a clean tagged checkout.
2. Confirm the same tag builds an equivalent unsigned APK in an F-Droid build environment.
3. Extract the release APK signing certificate fingerprint.
4. Add `Binaries` and `AllowedAPKSigningKeys` to `fdroiddata`.
5. Use F-Droid signature copying only after reproducibility is confirmed.

## Submission Checklist

- Confirm the release commit is tagged as `vX.Y.Z`.
- Confirm `npm run fdroid:changelog:check` passes.
- Confirm `npm run android:assemble:release` builds from a clean checkout without local signing secrets.
- Fork `fdroiddata`.
- Copy `docs/fdroiddata/metadata/com.hatvpip.receiver.yml` to `metadata/com.hatvpip.receiver.yml` in the `fdroiddata` fork.
- Run `fdroid lint com.hatvpip.receiver`.
- Run `fdroid checkupdates --allow-dirty com.hatvpip.receiver`.
- Run or let GitLab CI run `fdroid build com.hatvpip.receiver`.
- Open a merge request against `fdroid/fdroiddata`.

Do not add developer-signed binary verification metadata to the first submission unless reproducible builds have already been proven.

## Local Validation Notes

Step 4 local validation for `1.31.44` used a temporary `fdroiddata` checkout and `fdroidserver 2.4.5`.

Validated commands:

```sh
fdroid lint com.hatvpip.receiver
fdroid checkupdates --allow-dirty com.hatvpip.receiver
fdroid build -v -l com.hatvpip.receiver
```

Results:

- `fdroid lint com.hatvpip.receiver` passed.
- `fdroid checkupdates --allow-dirty com.hatvpip.receiver` exited successfully. The local `fdroiddata` config printed unrelated `serverwebroot` environment warnings.
- `fdroid build -v -l com.hatvpip.receiver` built `1.31.44` successfully from tag `v1.31.44`.

Local setup notes:

- The pip-installed `fdroidserver` package did not include `gradlew-fdroid`, so local validation used the helper from `https://gitlab.com/fdroid/gradlew-fdroid`.
- Docker was installed but not running locally, so the official container/buildserver route was not used.
- The F-Droid scanner removed the checked-in Gradle wrapper as expected; the build succeeded through F-Droid's own Gradle helper.

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

Default F-Droid publishing signs APKs with F-Droid's key. Users who installed the GitHub or Play build cannot update in-place to an F-Droid-signed build; they must uninstall and reinstall.

Developer-signed F-Droid publishing requires reproducible APK builds and extra `fdroiddata` signature metadata. Treat that as a later hardening pass after the first F-Droid build succeeds.

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

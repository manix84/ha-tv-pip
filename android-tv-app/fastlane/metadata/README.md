# Android Store Metadata 📺

This folder contains Play Store listing metadata drafts for the Android TV receiver app.

These files are also used as upstream metadata for F-Droid.

- They do not upload to Play Console by themselves.
- They do not configure Play deployment; AAB upload is controlled by GitHub release workflow secrets and variables.
- F-Droid can copy this metadata from the source repo after the app is accepted into `fdroiddata`.
- They should stay aligned with `docs/play-store.md`.
- Screenshots and graphics are tracked in `docs/play-store-assets.md`.
- The current feature graphic placeholder is `docs/assets/play-store-feature-graphic.jpg`.
- F-Droid release notes are generated from `WHATSNEW.md` with `npm run fdroid:changelog`.

The initial locale is `en-US`. Additional Play Store locales can be added after the English listing has been reviewed.

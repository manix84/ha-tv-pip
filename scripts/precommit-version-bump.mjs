#!/usr/bin/env node
import {
  applyVersionBump,
  androidVersionCodeForVersion,
  decideAutomaticBump,
  getStagedFile,
  getStagedFiles,
  getStagedRootVersion,
  hasAlreadyStagedVersionBump,
  stageFiles,
  validateBumpType
} from "./version-utils.mjs";

function missingReleaseMetadata(version) {
  const missing = [];
  const whatsNew = getStagedFile("WHATSNEW.md") ?? "";
  const headingPattern = new RegExp(`^##\\s+${version.replaceAll(".", "\\.")}\\b`, "m");

  if (!headingPattern.test(whatsNew)) {
    missing.push(`WHATSNEW.md entry for ${version}`);
  }

  const versionCode = androidVersionCodeForVersion(version);
  const fdroidChangelog =
    `android-tv-app/fastlane/metadata/android/en-US/changelogs/${versionCode}.txt`;
  if (getStagedFile(fdroidChangelog) === null) {
    missing.push(`F-Droid changelog ${fdroidChangelog}`);
  }

  return missing;
}

function stopForReleaseMetadata(version, missing) {
  console.error("");
  console.error(`Commit paused: release metadata is required for ${version}.`);
  for (const item of missing) {
    console.error(`- Missing staged ${item}`);
  }
  console.error("");
  console.error("Add the WHATSNEW.md entry, then run:");
  console.error("  npm run version:android-code");
  console.error("  npm run fdroid:changelog");
  console.error("Stage those updates and retry the commit.");
  process.exit(1);
}

try {
  const manualBump = validateBumpType(process.env.VERSION_BUMP);

  if (manualBump === "major" && process.env.ALLOW_MAJOR_BUMP !== "true") {
    console.error("Version bump failed: VERSION_BUMP=major requires ALLOW_MAJOR_BUMP=true.");
    console.error("Example: VERSION_BUMP=major ALLOW_MAJOR_BUMP=true git commit -m \"Rewrite architecture\"");
    process.exit(1);
  }

  const stagedFiles = getStagedFiles();

  if (manualBump === "none") {
    console.log("Version bump: skipped");
    console.log("Reason: VERSION_BUMP=none");
    process.exit(0);
  }

  if (hasAlreadyStagedVersionBump()) {
    const stagedVersion = getStagedRootVersion();
    const missing = stagedVersion ? missingReleaseMetadata(stagedVersion) : [];
    if (stagedVersion && missing.length > 0) {
      stopForReleaseMetadata(stagedVersion, missing);
    }
    console.log("Version bump: skipped");
    console.log("Reason: version bump and release metadata already staged");
    process.exit(0);
  }

  const decision = manualBump === "auto"
    ? decideAutomaticBump(stagedFiles)
    : { bump: manualBump, reason: `VERSION_BUMP=${manualBump}` };

  if (decision.bump === "none") {
    console.log("Version bump: skipped");
    console.log(`Reason: ${decision.reason}`);
    process.exit(0);
  }

  const result = applyVersionBump(decision.bump);
  stageFiles(result.updatedFiles);

  console.log(`Version bump: ${decision.bump}`);
  console.log(`Reason: ${decision.reason}`);
  console.log(`Updated: ${result.oldVersion} -> ${result.newVersion}`);
  console.log(`Files updated: ${result.updatedFiles.length > 0 ? result.updatedFiles.join(", ") : "none"}`);
  const missing = missingReleaseMetadata(result.newVersion);
  if (missing.length > 0) {
    stopForReleaseMetadata(result.newVersion, missing);
  }
} catch (error) {
  console.error(`Version bump failed: ${error.message}`);
  process.exit(1);
}

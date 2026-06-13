#!/usr/bin/env node
import {
  applyVersionBump,
  decideAutomaticBump,
  getStagedFiles,
  hasAlreadyStagedVersionBump,
  stageFiles,
  validateBumpType
} from "./version-utils.mjs";

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
    console.log("Version bump: skipped");
    console.log("Reason: version bump already staged");
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
} catch (error) {
  console.error(`Version bump failed: ${error.message}`);
  process.exit(1);
}

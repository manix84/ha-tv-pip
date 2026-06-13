#!/usr/bin/env node
import {
  applyVersionBump,
  validateBumpType
} from "./version-utils.mjs";

const requested = process.argv[2];
const bumpType = validateBumpType(requested);

if (bumpType === "auto" || bumpType === "none") {
  console.error("Usage: node scripts/bump-version.mjs patch|minor|major");
  process.exit(1);
}

if (bumpType === "major" && process.env.ALLOW_MAJOR_BUMP !== "true") {
  console.error("MAJOR bumps are protected. Re-run with ALLOW_MAJOR_BUMP=true if this is intentional.");
  process.exit(1);
}

const result = applyVersionBump(bumpType);

console.log(`Version bump: ${bumpType}`);
console.log(`Updated: ${result.oldVersion} -> ${result.newVersion}`);
console.log(`Files updated: ${result.updatedFiles.length > 0 ? result.updatedFiles.join(", ") : "none"}`);

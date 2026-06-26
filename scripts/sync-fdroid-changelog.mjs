import { existsSync, mkdirSync, readFileSync, writeFileSync } from "node:fs";
import { dirname } from "node:path";
import { ANDROID_BUILD_PATH, readRootVersion } from "./version-utils.mjs";

const checkOnly = process.argv.includes("--check");
const version = readRootVersion();
const buildGradle = readFileSync(ANDROID_BUILD_PATH, "utf8");
const versionCodeMatch = buildGradle.match(/versionCode\s*=\s*(\d+)/);

if (!versionCodeMatch) {
  console.error(`Could not find Android versionCode in ${ANDROID_BUILD_PATH}`);
  process.exit(1);
}

const versionCode = versionCodeMatch[1];
const changelogPath = `android-tv-app/fastlane/metadata/android/en-US/changelogs/${versionCode}.txt`;
const whatsNew = readFileSync("WHATSNEW.md", "utf8");
const headingPattern = new RegExp(`^##\\s+${version.replaceAll(".", "\\.")}\\b[^\\n]*\\n`, "m");
const headingMatch = whatsNew.match(headingPattern);

if (!headingMatch || headingMatch.index === undefined) {
  console.error(`WHATSNEW.md does not contain an entry for ${version}`);
  process.exit(1);
}

const entryStart = headingMatch.index + headingMatch[0].length;
const nextHeadingIndex = whatsNew.slice(entryStart).search(/^##\s+\d+\.\d+\.\d+\b/m);
const entryEnd = nextHeadingIndex === -1 ? whatsNew.length : entryStart + nextHeadingIndex;
const changelog = whatsNew
  .slice(entryStart, entryEnd)
  .trim()
  .split("\n")
  .map((line) => line.trimEnd())
  .filter(Boolean)
  .join("\n");

if (changelog.length > 500) {
  console.error(
    `F-Droid changelog for ${version} is ${changelog.length} characters; maximum is 500.`
  );
  process.exit(1);
}

if (checkOnly) {
  if (!existsSync(changelogPath)) {
    console.error(`Missing F-Droid changelog: ${changelogPath}`);
    process.exit(1);
  }

  const current = readFileSync(changelogPath, "utf8").trim();
  if (current !== changelog) {
    console.error(`F-Droid changelog is stale: ${changelogPath}`);
    console.error("Run `npm run fdroid:changelog` before release.");
    process.exit(1);
  }

  console.log(`F-Droid changelog ${changelogPath} matches ${version}`);
  process.exit(0);
}

mkdirSync(dirname(changelogPath), { recursive: true });
writeFileSync(changelogPath, `${changelog}\n`);
console.log(`Updated F-Droid changelog for ${version}: ${changelogPath}`);

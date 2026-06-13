import { existsSync, readFileSync } from "node:fs";

const rootPackage = JSON.parse(readFileSync("package.json", "utf8"));
const androidPackage = JSON.parse(readFileSync("android-tv-app/package.json", "utf8"));
const androidBuild = readFileSync("android-tv-app/app/build.gradle.kts", "utf8");
const whatsNew = readFileSync("WHATSNEW.md", "utf8");

const expectedVersion = rootPackage.version;
const versionNameMatch = androidBuild.match(/versionName\s*=\s*"([^"]+)"/);
const haManifestPath = "ha-integration/custom_components/ha_tv_pip/manifest.json";

const failures = [];
const warnings = [];

if (!expectedVersion || typeof expectedVersion !== "string") {
  failures.push("Root package.json must contain a string version");
} else if (!/^\d+\.\d+\.\d+(?:[-+][0-9A-Za-z.-]+)?$/.test(expectedVersion)) {
  failures.push(`Root package.json version '${expectedVersion}' is not valid semver`);
}

if (androidPackage.version !== expectedVersion) {
  failures.push(
    `android-tv-app/package.json version ${androidPackage.version} does not match root ${expectedVersion}`
  );
}

if (!versionNameMatch) {
  failures.push("Could not find Android versionName in android-tv-app/app/build.gradle.kts");
} else if (versionNameMatch[1] !== expectedVersion) {
  failures.push(
    `Android versionName ${versionNameMatch[1]} does not match root ${expectedVersion}`
  );
}

if (!whatsNew.includes(`## ${expectedVersion} `)) {
  warnings.push(`WHATSNEW.md does not contain an entry for ${expectedVersion}`);
}

if (existsSync(haManifestPath)) {
  const haManifest = JSON.parse(readFileSync(haManifestPath, "utf8"));
  if (haManifest.version !== expectedVersion) {
    failures.push(
      `${haManifestPath} version ${haManifest.version ?? "<missing>"} does not match root ${expectedVersion}`
    );
  }
} else {
  warnings.push(
    `TODO: ${haManifestPath} does not exist yet; Home Assistant integration version sync will be enforced once the integration is implemented`
  );
}

if (failures.length > 0) {
  console.error("Version check failed:");
  for (const failure of failures) {
    console.error(`- ${failure}`);
  }
  process.exit(1);
}

for (const warning of warnings) {
  console.warn(`Warning: ${warning}`);
}

console.log(`Version check passed for ${expectedVersion}`);

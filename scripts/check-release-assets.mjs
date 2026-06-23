import { existsSync, readFileSync } from "node:fs";
import { join, resolve } from "node:path";
import { spawnSync } from "node:child_process";

const rootPackage = JSON.parse(readFileSync("package.json", "utf8"));
const version = rootPackage.version;
const distDir = resolve(process.argv[2] ?? "dist");

if (!version || typeof version !== "string") {
  throw new Error("Root package.json must contain a version string");
}

const expectedAssets = {
  debugApk: `ha-tv-pip-android-debug-v${version}.apk`,
  releaseApk: `ha-tv-pip-android-release-v${version}.apk`,
  releaseBundle: `ha-tv-pip-android-release-v${version}.aab`,
  manualIntegrationZip: `ha-tv-pip-integration-v${version}.zip`,
  hacsIntegrationZip: "ha-tv-pip-integration.zip"
};

const failures = [];

function assetPath(name) {
  return join(distDir, name);
}

function requireFile(name) {
  const path = assetPath(name);
  if (!existsSync(path)) {
    failures.push(`Missing release asset: ${name}`);
  }
  return path;
}

function zipEntries(path) {
  const result = spawnSync("unzip", ["-Z1", path], {
    encoding: "utf8"
  });

  if (result.error) {
    failures.push(`Failed to inspect ${path}: ${result.error.message}`);
    return [];
  }

  if (result.status !== 0) {
    failures.push(`Failed to inspect ${path}: ${result.stderr.trim() || result.stdout.trim()}`);
    return [];
  }

  return result.stdout
    .split("\n")
    .map((entry) => entry.trim())
    .filter(Boolean);
}

function readZipEntry(path, entry) {
  const result = spawnSync("unzip", ["-p", path, entry], {
    encoding: "utf8"
  });

  if (result.error || result.status !== 0) {
    failures.push(`Failed to read ${entry} from ${path}`);
    return null;
  }

  return result.stdout;
}

function expectEntry(entries, entry, archiveName) {
  if (!entries.includes(entry)) {
    failures.push(`${archiveName} is missing ${entry}`);
  }
}

function expectNoEntry(entries, predicate, message) {
  if (entries.some(predicate)) {
    failures.push(message);
  }
}

function checkApk(name) {
  const path = requireFile(name);
  if (!existsSync(path)) {
    return;
  }

  const entries = zipEntries(path);
  expectEntry(entries, "AndroidManifest.xml", name);
  expectEntry(entries, "resources.arsc", name);

  if (!entries.some((entry) => /^classes(\d+)?\.dex$/.test(entry))) {
    failures.push(`${name} is missing classes.dex`);
  }
}

function checkAab(name) {
  const path = requireFile(name);
  if (!existsSync(path)) {
    return;
  }

  const entries = zipEntries(path);
  expectEntry(entries, "BundleConfig.pb", name);
  expectEntry(entries, "base/manifest/AndroidManifest.xml", name);

  if (!entries.some((entry) => /^base\/dex\/classes(\d+)?\.dex$/.test(entry))) {
    failures.push(`${name} is missing base dex classes`);
  }
}

function checkManualIntegrationZip(name) {
  const path = requireFile(name);
  if (!existsSync(path)) {
    return;
  }

  const entries = zipEntries(path);
  const manifestPath = "custom_components/ha_tv_pip/manifest.json";

  expectEntry(entries, manifestPath, name);
  expectEntry(entries, "custom_components/ha_tv_pip/config_flow.py", name);
  expectEntry(entries, "custom_components/ha_tv_pip/README.md", name);
  expectNoEntry(
    entries,
    (entry) => entry === "manifest.json",
    `${name} must not place manifest.json at archive root`
  );
  expectNoIgnoredEntries(entries, name);
  expectManifestVersion(path, manifestPath, name);
}

function checkHacsIntegrationZip(name) {
  const path = requireFile(name);
  if (!existsSync(path)) {
    return;
  }

  const entries = zipEntries(path);

  expectEntry(entries, "manifest.json", name);
  expectEntry(entries, "config_flow.py", name);
  expectEntry(entries, "README.md", name);
  expectEntry(entries, "icon.png", name);
  expectEntry(entries, "brand/icon.png", name);
  expectNoEntry(
    entries,
    (entry) => entry.startsWith("custom_components/"),
    `${name} must be rooted at the integration folder for HACS zip_release installs`
  );
  expectNoIgnoredEntries(entries, name);
  expectManifestVersion(path, "manifest.json", name);
}

function expectNoIgnoredEntries(entries, archiveName) {
  const ignoredSegments = [".git", "node_modules", "dist", "__pycache__", ".DS_Store"];
  for (const segment of ignoredSegments) {
    expectNoEntry(
      entries,
      (entry) => entry.split("/").includes(segment),
      `${archiveName} must not include ${segment}`
    );
  }
}

function expectManifestVersion(path, manifestPath, archiveName) {
  const manifestContent = readZipEntry(path, manifestPath);
  if (!manifestContent) {
    return;
  }

  const manifest = JSON.parse(manifestContent);
  if (manifest.version !== version) {
    failures.push(
      `${archiveName} manifest version ${manifest.version ?? "<missing>"} does not match root ${version}`
    );
  }
}

checkApk(expectedAssets.debugApk);
checkApk(expectedAssets.releaseApk);
checkAab(expectedAssets.releaseBundle);
checkManualIntegrationZip(expectedAssets.manualIntegrationZip);
checkHacsIntegrationZip(expectedAssets.hacsIntegrationZip);

if (failures.length > 0) {
  console.error("Release asset check failed:");
  for (const failure of failures) {
    console.error(`- ${failure}`);
  }
  process.exit(1);
}

console.log(`Release asset check passed for ${version}`);

#!/usr/bin/env node
import { readFileSync, writeFileSync } from "node:fs";
import {
  ANDROID_BUILD_PATH,
  androidVersionCodeForVersion,
  readRootVersion
} from "./version-utils.mjs";

const checkOnly = process.argv.includes("--check");
const version = readRootVersion();
const expectedVersionCode = androidVersionCodeForVersion(version);
const current = readFileSync(ANDROID_BUILD_PATH, "utf8");
const versionCodeMatch = current.match(/versionCode\s*=\s*(\d+)/);

if (!versionCodeMatch) {
  console.error(`Could not find Android versionCode in ${ANDROID_BUILD_PATH}`);
  process.exit(1);
}

const currentVersionCode = Number(versionCodeMatch[1]);
if (currentVersionCode === expectedVersionCode) {
  console.log(`Android versionCode ${currentVersionCode} matches ${version}`);
  process.exit(0);
}

if (checkOnly) {
  console.error(
    `Android versionCode ${currentVersionCode} does not match expected ${expectedVersionCode} for ${version}.`
  );
  console.error("Run `npm run version:android-code` before building release AABs.");
  process.exit(1);
}

const next = current.replace(/versionCode\s*=\s*\d+/, `versionCode = ${expectedVersionCode}`);
writeFileSync(ANDROID_BUILD_PATH, next);
console.log(`Updated Android versionCode: ${currentVersionCode} -> ${expectedVersionCode}`);

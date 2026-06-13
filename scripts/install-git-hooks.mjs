import { spawnSync } from "node:child_process";

function runGit(args) {
  return spawnSync("git", args, {
    encoding: "utf8",
    stdio: ["ignore", "pipe", "pipe"],
  });
}

const gitDir = runGit(["rev-parse", "--git-dir"]);

if (gitDir.status !== 0) {
  console.warn("Skipping Git hook setup because this is not a Git checkout.");
  process.exit(0);
}

const config = runGit(["config", "core.hooksPath", ".githooks"]);

if (config.status !== 0) {
  console.error("Failed to configure native Git hooks.");
  console.error(config.stderr.trim());
  process.exit(config.status ?? 1);
}

console.log("Configured native Git hooks from .githooks");

import { spawnSync } from "node:child_process";
import { existsSync } from "node:fs";
import { join } from "node:path";

const isWindows = process.platform === "win32";
const integrationDir = "ha-integration";
const requirementsPath = join(integrationDir, "requirements-dev.txt");
const integrationVenvDir = join(integrationDir, ".venv");
const integrationVenvPython = join(
  integrationVenvDir,
  isWindows ? "Scripts/python.exe" : "bin/python",
);

function run(label, command, args) {
  console.log(`\n==> ${label}`);

  const result = spawnSync(command, args, {
    stdio: "inherit",
  });

  if (result.status !== 0) {
    process.exit(result.status ?? 1);
  }
}

if (!existsSync(requirementsPath)) {
  console.log("No Home Assistant integration Python requirements found.");
  process.exit(0);
}

if (!existsSync(integrationVenvPython)) {
  run("Creating Home Assistant integration Python virtualenv", "python3", [
    "-m",
    "venv",
    integrationVenvDir,
  ]);
}

run("Updating Home Assistant integration virtualenv pip", integrationVenvPython, [
  "-m",
  "pip",
  "install",
  "--upgrade",
  "pip",
]);

run("Installing Home Assistant integration Python dev tools", integrationVenvPython, [
  "-m",
  "pip",
  "install",
  "-r",
  requirementsPath,
]);

console.log("\nHome Assistant Python tools are installed in ha-integration/.venv.");

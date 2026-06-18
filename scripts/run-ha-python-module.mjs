import { spawnSync } from "node:child_process";
import { existsSync } from "node:fs";
import { delimiter, dirname, join } from "node:path";

const isWindows = process.platform === "win32";
const integrationDir = process.cwd().endsWith("ha-integration")
  ? process.cwd()
  : join(process.cwd(), "ha-integration");
const repoRoot = dirname(integrationDir);
const venvPython = join(integrationDir, ".venv", isWindows ? "Scripts/python.exe" : "bin/python");

if (!existsSync(venvPython)) {
  console.error("Home Assistant integration virtualenv not found.");
  console.error("Run `npm run install:all` from the repository root first.");
  process.exit(1);
}

const [moduleName, ...moduleArgs] = process.argv.slice(2);

if (!moduleName) {
  console.error("Missing Python module name.");
  process.exit(1);
}

const result = spawnSync(venvPython, ["-m", moduleName, ...moduleArgs], {
  cwd: integrationDir,
  env: {
    ...process.env,
    MYPYPATH: [repoRoot, process.env.MYPYPATH].filter(Boolean).join(delimiter),
    PYTHONPATH: [repoRoot, process.env.PYTHONPATH].filter(Boolean).join(delimiter),
  },
  stdio: "inherit",
});

process.exit(result.status ?? 1);

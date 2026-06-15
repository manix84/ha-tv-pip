# Security Policy 🛡️

## Supported Versions 📌

HA TV PiP is pre-release software. Security fixes are handled on the main development line.

## Reporting a Vulnerability 🔐

Please do not open public issues for sensitive security reports.

If a private maintainer contact is available in the GitHub repository, use that path. Otherwise, contact the repository owner directly and include:

- A clear description of the issue.
- Steps to reproduce.
- Affected files or versions.
- Any known impact.

## Current Security Scope 🧪

The receiver exposes a local HTTP endpoint on the LAN. Since Stage 4, `/show` and `/close` require pairing and bearer-token authentication.

Pairing uses a six-digit code shown on the TV. The pairing code is not returned over HTTP, and existing pairings cannot be replaced remotely; use `Reset Pairing` on the TV app first.

Not yet implemented:

- Camera entity service calls.
- Snapshot display.
- WebRTC.
- Remote receiver mode.
- Play Store or HACS distribution hardening.

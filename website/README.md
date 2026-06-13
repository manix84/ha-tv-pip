# HA TV PiP Website 🌐

[![Website 🌐](https://github.com/manix84/hassio-pip/actions/workflows/website.yml/badge.svg)](https://github.com/manix84/hassio-pip/actions/workflows/website.yml)

Promotional Vite website for HA TV PiP.

## Tech Stack 🛠️

- Vite
- React
- TypeScript
- SCSS Modules

## Development 🚀

From the monorepo root:

```sh
npm run website:dev
```

From this directory:

```sh
npm run dev
```

## Build 📦

```sh
npm run build
```

The site is static and ready for future GitHub Pages deployment.

## Deployment 🌍

The GitHub Actions website workflow builds the site on pull requests and deploys `website/dist` to GitHub Pages on pushes to `main`.

## Version 📌

The displayed version comes from the root `package.json` at build time through `vite.config.ts`.

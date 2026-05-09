# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TextShare is a Vue 3 + Vite + TypeScript frontend for a LAN-based text sharing tool. Users can share text content via generated links that expire after 24 hours.

## Tech Stack

- **Vue 3** with Composition API (`<script setup>`)
- **Vite** for build tooling
- **TypeScript** for type safety
- **Tailwind CSS** for styling (amber primary color, dark mode via `.dark` class)
- **Vue Router** for client-side routing

## Commands

```bash
npm run dev      # Start dev server
npm run build    # Build for production (runs vue-tsc type check + vite build)
npm run preview  # Preview production build locally
```

## Architecture

```
src/
├── views/          # Page components (routes)
│   ├── SendView.vue      # Main page - compose and share text
│   ├── ReceiveView.vue   # /s/:id - view shared text
│   └── ExpiredView.vue   # /expired - expired link page
├── components/     # Reusable Vue components
├── composables/    # Vue composables (useTheme, useClipboard)
├── router/         # Vue Router configuration
├── types/          # TypeScript type definitions
└── style.css       # Global styles and CSS variables
```

## Key Alias

`@/` is aliased to `src/` for imports.

## Routes

| Path | Component | Description |
|------|-----------|-------------|
| `/` | SendView | Create and share text |
| `/s/:id` | ReceiveView | View shared text by ID |
| `/expired` | ExpiredView | Shown when link is too old |

## Backend Integration

API calls are currently mocked in the code with comments showing the expected endpoints:
- `GET /api/texts/:id` - fetch text by ID
- `POST /api/texts/:id/view` - increment view count

## Theme System

Dark mode uses Tailwind's `.dark` class strategy. CSS variables (`--bg-primary`, `--text-primary`, etc.) are used for theming in components.

# NexaAPI

The Foundation API for **NexaCore** — a professional, lightweight,
extensible API plugin for Paper / Spigot servers.

**Author:** Allent_tw
**Version:** 1.0.0
**Platform:** Paper / Spigot (1.13+)

## Overview

NexaAPI provides reusable managers, utilities, and extension points that
NexaCore and other Nexa plugins can build on. The plugin itself is
**not a utility plugin** — it's the shared foundation.

## Features

| System | Status |
|--------|--------|
| Manager Registry | ✅ Complete |
| Configuration Manager | ✅ Complete |
| Message Manager | ✅ Complete |
| Placeholder Manager (optional PlaceholderAPI bridge) | ✅ Complete |
| Storage Manager (YAML; SQLite/MySQL extension points) | ✅ Complete |
| Command API (with subcommands + tab completion) | ✅ Complete |
| Custom Event Bus | ✅ Complete |
| Player Utilities | ✅ Complete |
| Color/Hex Util | ✅ Complete |
| Cooldown Util | ✅ Complete |
| Menu API | ✅ Foundation |
| NPC / Hologram / Scoreboard / Tab / Script | ✅ Interfaces (implementation by NexaCore) |

## Building

```bash
mvn clean package
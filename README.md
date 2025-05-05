# BetterTablist

A powerful and flexible Minecraft plugin for creating animated and personalized tablists across all your servers — using shared global configurations.

---

## ✅ Features

- Animated headers and footers
- Global config system for entire networks
- Server tag-based configuration selection
- Smooth integration with PlaceholderAPI
- Simple reload command
- Custom placeholders (`{player}`, `{online}`, `{time}`, etc.)
- Randomized animation options

---

## 🚀 Getting Started

### 1. Plugin Installation

- [Download](https://github.com/felixstaude/BetterTablist/releases) the plugin and place the `BetterTablist.jar` file into your server's `plugins/` folder.
- Restart your server.
- After the restart, all necessary configuration files will be generated automatically.

---

### 2. Configuration

#### Local config (config.yml)

Your local `config.yml` contains the default configuration. It also defines the `server_tag` and the `global_config` path:

```yaml
server_tag: citybuild
global_config: "../BetterTablistGlobal/"
```

This means your plugin will try to load a shared global file for the tag `citybuild`, such as `citybuild.yml` in the folder `../BetterTablistGlobal/`.

#### Global config mapping (globalConfig.yml)

Inside your global config folder, you’ll find a `globalConfig.yml` file with the structure:

```yaml
tag_mappings:
  citybuild: citybuild.yml
  bedwars: minigame.yml
  skywars: minigame.yml
```

Each server tag will load the assigned file. Multiple tags can share the same configuration.

#### Fallback template

If no server tag is found or the config file is missing, a fallback file `template.yml` will be used.

---

## 📋 Example: citybuild.yml

```yaml
use_animated_header: true
animated_header_interval_ticks: 100
animated_header:
  -
    - "&aWelcome, {player}!"
    - "&7Players online: {online}"
    - "&7Time: {time}"
header:
  - "&6Fallback Header"

use_animated_footer: false
footer:
  - "&eJoin our Discord: discord.gg/example"
```

---

## 🔧 PlaceholderAPI Support

Make sure PlaceholderAPI is installed. The plugin will automatically register custom placeholders.

You can use placeholders such as:

- `{player}` — Player name
- `{online}` — Number of online players
- `%tablist_world%` — Player's world
- `%tablist_hello%` — Greeting

---

## 🔁 Reloading

Use `/tablistreload` to reload your configuration while the server is running.

Permission required: `tablist.reload`

---

## 🧪 Compatibility Table

| Minecraft Version | Plugin Version | Status     |
|-------------------|----------------|------------|
| 1.21.4            | v1.0           | ✅ Tested   |
| future            | TBD            | ⏳ Planned  |

---

## ⚠️ Notes

- If a global config is missing or invalid, a warning will appear at most every 2 minutes to keep logs clean.
- The fallback config `template.yml` will be generated automatically if missing.
- You can define your own placeholders and use them freely in all tablist fields.

---

## 💬 Support & Feedback

- Open an issue here on GitHub
---

## 📜 License

MIT — use this plugin freely, but don't forget to give credit 🙂

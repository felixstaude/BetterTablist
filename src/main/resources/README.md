Tablist Plugin - Developer Documentation
========================================

Author: Felix Staude
Version: 1.0
Minecraft API: 1.20+
Soft Dependencies: PlaceholderAPI

----------------------------------------
📁 Plugin Structure
----------------------------------------

- core/
  - TablistCustomizer.java     → Main plugin class (onEnable, config watcher, modules)
  - TablistAPI.java            → Placeholder parsing (custom + PAPI)
  - PlaceholderRegistry.java   → Built-in placeholder registration

- tab/
  - TablistManager.java        → Controls header/footer rendering and animation

- commands/
  - TablistReloadCommand.java  → /tablistreload command (reloads config and updates tablist)

- placeholders/
  - TablistExpansion.java      → PlaceholderAPI Expansion (e.g. %tablist_world%)

----------------------------------------
🛠 Features
----------------------------------------

✔ Animated Tablist header/footer (configurable tick delay)
✔ Random order for animations (optional)
✔ Support for multiline header/footer frames
✔ Fallback header/footer if animation is disabled
✔ PlaceholderAPI integration (%tablist_*% and third-party expansions)
✔ Own custom placeholders with {curly} syntax
✔ Modular feature system via "modules:" section in config.yml
✔ Auto reload if config file changes
✔ /tablistreload command for manual reload

----------------------------------------
⚙ config.yml Overview
----------------------------------------

modules:
  animation: true             # Enables tick-based animation
  random_order: true          # Enables random frame selection
  easter_eggs: false          # Reserved for future features

use_animated_header: true     # Enables animated header frames
animated_header_interval_ticks: 100
animated_header_random_order: false

use_animated_footer: true
animated_footer_interval_ticks: 200
animated_footer_random_order: true

update_interval_ticks: 10     # Update all player tablists every X ticks

animated_header:
  - ["Line 1", "Line 2", "Line 3"]
  - ["Other Header Line 1", "Line 2"]

animated_footer:
  - ["Line 1", "Line 2"]
  - ["Other Footer Line 1"]

header:
  - "Static fallback header line"

footer:
  - "Static fallback footer line"

----------------------------------------
🔌 PAPI Placeholder Usage
----------------------------------------

Register new placeholder:
  TablistAPI.registerPlaceholder("key", player -> "value");

Supports:
  - %tablist_world% → World name of the player
  - %tablist_hello% → Simple hello message
  - Your own logic

----------------------------------------
✅ Permissions
----------------------------------------

tablist.reload → Allows usage of /tablistreload command

----------------------------------------
💡 Future Ideas
----------------------------------------

- Global config support for networks
- GUI config editor
- Auto-enable modules via GUI or command
- More built-in placeholders (ping, tps, etc)
package org.felixstaude.tablist.core;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonalTablistManager {

    public static YamlConfiguration loadPlayerFile(Player player) {
        File file = getPlayerFile(player);
        if (!file.exists()) return null;
        return YamlConfiguration.loadConfiguration(file);
    }

    public static boolean hasCustomTablist(Player player) {
        YamlConfiguration config = loadPlayerFile(player);
        return config != null && config.getBoolean("use_personal_tablist", false);
    }

    public static List<String> getHeader(Player player) {
        YamlConfiguration config = loadPlayerFile(player);
        return config == null ? null : config.getStringList("header");
    }

    public static List<String> getFooter(Player player) {
        YamlConfiguration config = loadPlayerFile(player);
        return config == null ? null : config.getStringList("footer");
    }

    public static void setSetting(Player player, String key, Object value) {
        File file = getPlayerFile(player);
        YamlConfiguration config = loadPlayerFile(player);
        if (config == null) config = new YamlConfiguration();

        config.set(key, value);

        try {
            config.save(file);
        } catch (IOException e) {
            TablistCustomizer.getInstance().getLogger().warning("Failed to save personal tablist config for " + player.getName() + ": " + e.getMessage());
        }
    }

    public static boolean getBooleanSetting(Player player, String key) {
        YamlConfiguration config = loadPlayerFile(player);
        return config != null && config.getBoolean(key, false);
    }

    public static void setLine(Player player, String section, int line, String text) {
        File file = getPlayerFile(player);
        YamlConfiguration config = loadPlayerFile(player);
        if (config == null) config = new YamlConfiguration();

        List<String> lines = config.getStringList(section);
        if (lines == null) lines = new ArrayList<>();

        // Ensure list has enough size
        while (lines.size() <= line) {
            lines.add("");
        }

        lines.set(line, text);
        config.set(section, lines);

        try {
            config.save(file);
        } catch (IOException e) {
            TablistCustomizer.getInstance().getLogger().warning("Failed to save personal tablist config for " + player.getName() + ": " + e.getMessage());
        }
    }

    public static void clearTablist(Player player) {
        File file = getPlayerFile(player);
        YamlConfiguration config = loadPlayerFile(player);
        if (config == null) config = new YamlConfiguration();

        config.set("header", null);
        config.set("footer", null);

        try {
            config.save(file);
        } catch (IOException e) {
            TablistCustomizer.getInstance().getLogger().warning("Failed to clear tablist for " + player.getName() + ": " + e.getMessage());
        }
    }

    private static File getPlayerFile(Player player) {
        String uuid = player.getUniqueId().toString();
        String path = TablistCustomizer.getInstance().getConfig().getString("global_personal_storage_path");
        File basePath = new File(path);

        if (!basePath.exists()) basePath.mkdirs();
        return new File(basePath, uuid + ".yml");
    }
}

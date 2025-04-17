package org.felixstaude.tablist.core;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class TablistGlobalPersonalConfig {

    private static YamlConfiguration config;

    public static void load(File file) {
        if (!file.exists()) return;
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static boolean isPersonalTablistAllowed() {
        return config != null && config.getBoolean("enable_personal_tablist", false);
    }

    public static boolean isAllowedOnTag(String tag) {
        if (config == null) return false;
        List<String> allowedTags = config.getStringList("allowed_server_tags_for_personal_tablist");
        return allowedTags.contains(tag);
    }
}

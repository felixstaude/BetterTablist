package org.felixstaude.tablist.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalConfigUtil {

    private static final Map<String, String> tagToFileMap = new HashMap<>();
    private static final String DEFAULT_PATH = "../../network/tablists/";
    private static final String DEFAULT_MAPPING_FILE = "globalConfig.yml";
    private static final String TEMPLATE_FILE = "template.yml";

    private static File globalFolder;
    private static FileConfiguration fallbackConfig;

    private static long lastWarningTime = 0L;
    private static final long WARNING_INTERVAL = 2 * 60 * 1000; // 2 minutes

    public static void loadMappingsAndDefaults() {
        FileConfiguration localConfig = TablistCustomizer.getInstance().getConfig();
        globalFolder = new File(localConfig.getString("global_config", DEFAULT_PATH));
        File mappingFile = new File(globalFolder, DEFAULT_MAPPING_FILE);

        if (!mappingFile.exists()) createDefaultGlobalMapping(mappingFile);
        FileConfiguration mappingConfig = YamlConfiguration.loadConfiguration(mappingFile);

        if (mappingConfig.contains("tag_mappings")) {
            for (String tag : mappingConfig.getConfigurationSection("tag_mappings").getKeys(false)) {
                String filename = mappingConfig.getString("tag_mappings." + tag);
                tagToFileMap.put(tag.toLowerCase(), filename);
            }
        }

        File fallbackFile = new File(globalFolder, TEMPLATE_FILE);
        if (fallbackFile.exists()) {
            fallbackConfig = YamlConfiguration.loadConfiguration(fallbackFile);
        } else {
            createFallbackTemplate(fallbackFile);
            fallbackConfig = YamlConfiguration.loadConfiguration(fallbackFile);
        }
    }

    private static void createDefaultGlobalMapping(File file) {
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

            YamlConfiguration config = new YamlConfiguration();
            config.set("tag_mappings.citybuild", "citybuild.yml");
            config.set("tag_mappings.bedwars", "minigame.yml");
            config.set("tag_mappings.skywars", "minigame.yml");
            config.save(file);

            TablistCustomizer.getInstance().getLogger().info("Created default globalConfig.yml with basic mappings.");
        } catch (IOException e) {
            logWarning("Failed to create default globalConfig.yml: " + e.getMessage());
        }
    }

    private static void createFallbackTemplate(File file) {
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

            YamlConfiguration config = new YamlConfiguration();
            config.set("header", List.of("&c[Fallback] &fDefault header used!"));
            config.set("footer", List.of("&e[Fallback] &fPlease check your configuration."));
            config.set("use_animated_header", false);
            config.set("use_animated_footer", false);
            config.save(file);

            TablistCustomizer.getInstance().getLogger().info("Created default template.yml fallback configuration.");
        } catch (IOException e) {
            logWarning("Failed to create template.yml fallback file: " + e.getMessage());
        }
    }

    public static FileConfiguration getConfigForTagOrFallback(String tag) {
        if (tag == null || tag.isEmpty()) {
            logWarning("No server_tag set. Using fallback template configuration.");
            return fallbackConfig;
        }

        String fileName = tagToFileMap.get(tag.toLowerCase());
        if (fileName == null) {
            logWarning("Unknown server_tag '" + tag + "'. Using fallback template configuration.");
            return fallbackConfig;
        }

        File configFile = new File(globalFolder, fileName);
        if (!configFile.exists()) {
            logWarning("Config file '" + fileName + "' not found. Using fallback template configuration.");
            return fallbackConfig;
        }

        return YamlConfiguration.loadConfiguration(configFile);
    }

    private static void logWarning(String message) {
        long now = System.currentTimeMillis();
        if (now - lastWarningTime > WARNING_INTERVAL) {
            TablistCustomizer.getInstance().getLogger().warning("[BetterTablist] " + message);
            lastWarningTime = now;
        }
    }
}

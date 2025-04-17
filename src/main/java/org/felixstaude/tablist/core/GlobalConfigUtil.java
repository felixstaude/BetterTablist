package org.felixstaude.tablist.core;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GlobalConfigUtil {

    public static void createDefaultGlobalConfig(File file) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            YamlConfiguration config = new YamlConfiguration();
            config.set("enable_personal_tablist", true);
            config.set("personal_tablist_scope", "global");
            config.set("global_personal_storage_path", "../../network/tablists/tablists/");
            config.set("allowed_server_tags_for_personal_tablist", List.of("citybuild", "lobby"));
            config.save(file);

            TablistCustomizer.getInstance().getLogger().info("Created default global.yml in ../../network/tablists/");
        } catch (IOException e) {
            TablistCustomizer.getInstance().getLogger().warning("Failed to create global.yml: " + e.getMessage());
        }
    }
}

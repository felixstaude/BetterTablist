package org.felixstaude.tablist.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.felixstaude.tablist.commands.TablistReloadCommand;
import org.felixstaude.tablist.commands.TablistSettingsCommand;
import org.felixstaude.tablist.placeholders.TablistExpansion;

import java.io.File;

public final class TablistCustomizer extends JavaPlugin {

    private static TablistCustomizer instance;
    private long lastModifiedTime = 0L;
    private int configWatcherTaskId = -1;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        // 🔽 Lade globale Konfiguration für personalisierte Tablisten
        File globalPersonal = new File(new File(getDataFolder().getParentFile().getParent(), "../../network/tablist"), "global.yml");

        if (!globalPersonal.exists()) {
            getLogger().info("global.yml not found, creating default...");
            GlobalConfigUtil.createDefaultGlobalConfig(globalPersonal);
        }

        TablistGlobalPersonalConfig.load(globalPersonal);


        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TablistExpansion().register();
            TablistAPI.setUsePAPI(true);
            getLogger().info("Registered custom PlaceholderAPI expansion.");
        }

        registerConfigWatcher();
        getCommand("tablistreload").setExecutor(new TablistReloadCommand());
        getCommand("tablist").setExecutor(new TablistSettingsCommand());
        getCommand("tablist").setTabCompleter(new TablistSettingsCommand());

        getServer().getPluginManager().registerEvents(new TablistManager(), this);
        PlaceholderRegistry.registerDefaults();

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (isModuleEnabled("animation")) {
                TablistManager.tickAnimations();
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                TablistManager.updateTablist(player);
            }
        }, 0L, 1L);

        getLogger().info("Tablist plugin activated.");
    }

    public static TablistCustomizer getInstance() {
        return instance;
    }

    public void registerConfigWatcher() {
        File configFile = new File(getDataFolder(), "config.yml");
        lastModifiedTime = configFile.lastModified();

        configWatcherTaskId = Bukkit.getScheduler().runTaskTimer(this, () -> {
            long currentModified = configFile.lastModified();
            if (currentModified != lastModifiedTime) {
                lastModifiedTime = currentModified;
                getLogger().info("Tablist config file changes detected, automatic reload...");
                reloadConfig();
                TablistManager.resetAnimationState();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    TablistManager.updateTablist(player);
                }
            }
        }, 20L, 20L).getTaskId();
    }

    public boolean isModuleEnabled(String key) {
        return getConfig().getConfigurationSection("modules") != null &&
                getConfig().getBoolean("modules." + key, false);
    }
}
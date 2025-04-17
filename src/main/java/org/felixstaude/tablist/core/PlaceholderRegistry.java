package org.felixstaude.tablist.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderRegistry {

    public static void registerDefaults() {
        TablistAPI.registerPlaceholder("player", Player::getName);
        TablistAPI.registerPlaceholder("online", p -> String.valueOf(Bukkit.getOnlinePlayers().size()));
        TablistAPI.registerPlaceholder("time", player -> "??:??");
    }
}
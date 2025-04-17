package org.felixstaude.tablist.core;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TablistAPI {

    private static final Map<String, Function<Player, String>> placeholders = new HashMap<>();
    private static boolean usePAPI = false;

    public static void registerPlaceholder(String key, Function<Player, String> func) {
        placeholders.put(key.toLowerCase(), func);
    }

    public static void setUsePAPI(boolean use) {
        usePAPI = use;
    }

    public static String parse(String text, Player player) {
        if (text == null) return "";

        String result = text;

        for (Map.Entry<String, Function<Player, String>> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue().apply(player));
        }

        if (usePAPI) {
            result = PlaceholderAPI.setPlaceholders(player, result);
        }

        return ChatColor.translateAlternateColorCodes('&', result);
    }
}
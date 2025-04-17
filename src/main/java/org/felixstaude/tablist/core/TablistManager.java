package org.felixstaude.tablist.core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Random;

public class TablistManager implements Listener {

    private static final Random random = new Random();

    private static int headerTick = 0;
    private static int footerTick = 0;
    private static int currentHeaderIndex = 0;
    private static int currentFooterIndex = 0;

    public static void updateTablist(Player player) {
        FileConfiguration config = TablistCustomizer.getInstance().getConfig();

        if (!TablistCustomizer.getInstance().isModuleEnabled("personal_tablist")) {
            applyStandardTablist(player);
            return;
        }

        boolean enabledInGlobal = TablistGlobalPersonalConfig.isPersonalTablistAllowed();
        String serverTag = config.getString("server_tag");

        if (enabledInGlobal && TablistGlobalPersonalConfig.isAllowedOnTag(serverTag)
                && PersonalTablistManager.hasCustomTablist(player)) {

            List<String> header = PersonalTablistManager.getHeader(player);
            List<String> footer = PersonalTablistManager.getFooter(player);

            String headerString = header == null ? "" : TablistAPI.parse(String.join("\n", header), player);
            String footerString = footer == null ? "" : TablistAPI.parse(String.join("\n", footer), player);

            player.setPlayerListHeaderFooter(headerString, footerString);
            return;
        }

        applyStandardTablist(player);
    }

    public static void tickAnimations() {
        FileConfiguration config = TablistCustomizer.getInstance().getConfig();

        int headerInterval = config.getInt("animated_header_interval_ticks", 40);
        int footerInterval = config.getInt("animated_footer_interval_ticks", 40);

        headerTick++;
        footerTick++;

        if (headerTick >= headerInterval) {
            headerTick = 0;
            if (TablistCustomizer.getInstance().isModuleEnabled("random_order") &&
                    config.getBoolean("animated_header_random_order", false)) {
                List<?> raw = config.getList("animated_header");
                if (raw != null && !raw.isEmpty()) {
                    currentHeaderIndex = random.nextInt(raw.size());
                }
            } else {
                currentHeaderIndex++;
            }
        }

        if (footerTick >= footerInterval) {
            footerTick = 0;
            if (TablistCustomizer.getInstance().isModuleEnabled("random_order") &&
                    config.getBoolean("animated_footer_random_order", false)) {
                List<?> raw = config.getList("animated_footer");
                if (raw != null && !raw.isEmpty()) {
                    currentFooterIndex = random.nextInt(raw.size());
                }
            } else {
                currentFooterIndex++;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static String getCurrentHeader(Player player) {
        FileConfiguration config = TablistCustomizer.getInstance().getConfig();

        if (config.getBoolean("use_animated_header")) {
            List<?> raw = config.getList("animated_header");
            if (raw == null || raw.isEmpty()) return "";

            Object first = raw.get(0);
            if (first instanceof List) {
                List<List<String>> frames = (List<List<String>>) (List<?>) raw;
                List<String> currentFrame = frames.get(currentHeaderIndex % frames.size());
                return TablistAPI.parse(String.join("\n", currentFrame), player);
            } else if (first instanceof String) {
                List<String> frames = (List<String>) raw;
                return TablistAPI.parse(frames.get(currentHeaderIndex % frames.size()), player);
            }
        }

        List<String> lines = config.getStringList("header");
        return TablistAPI.parse(String.join("\n", lines), player);
    }

    @SuppressWarnings("unchecked")
    public static String getCurrentFooter(Player player) {
        FileConfiguration config = TablistCustomizer.getInstance().getConfig();

        if (config.getBoolean("use_animated_footer")) {
            List<?> raw = config.getList("animated_footer");
            if (raw == null || raw.isEmpty()) return "";

            Object first = raw.get(0);
            if (first instanceof List) {
                List<List<String>> frames = (List<List<String>>) (List<?>) raw;
                List<String> currentFrame = frames.get(currentFooterIndex % frames.size());
                return TablistAPI.parse(String.join("\n", currentFrame), player);
            } else if (first instanceof String) {
                List<String> frames = (List<String>) raw;
                return TablistAPI.parse(frames.get(currentFooterIndex % frames.size()), player);
            }
        }

        List<String> lines = config.getStringList("footer");
        return TablistAPI.parse(String.join("\n", lines), player);
    }

    public static void resetAnimationState() {
        headerTick = 0;
        footerTick = 0;
        currentHeaderIndex = 0;
        currentFooterIndex = 0;
    }

    public static void applyStandardTablist(Player player) {
        String header = getCurrentHeader(player);
        String footer = getCurrentFooter(player);
        player.setPlayerListHeaderFooter(header, footer);
    }

}
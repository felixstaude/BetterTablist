package org.felixstaude.tablist.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.felixstaude.tablist.core.TablistCustomizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TablistExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "tablist";
    }

    @Override
    public @NotNull String getAuthor() {
        return "FelixStaude";
    }

    @Override
    public @NotNull String getVersion() {
        return TablistCustomizer.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        if (identifier.equalsIgnoreCase("world")) {
            return player.getWorld().getName();
        }

        if (identifier.equalsIgnoreCase("hello")) {
            return "Hello " + player.getName() + " 👋";
        }

        return null;
    }
}
package org.felixstaude.tablist.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.felixstaude.tablist.core.TablistCustomizer;
import org.felixstaude.tablist.core.TablistManager;

public class TablistReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tablist.reload")) {
            sender.sendMessage("\u00a7cNo permission.");
            return true;
        }

        TablistCustomizer plugin = TablistCustomizer.getInstance();
        plugin.reloadConfig();
        TablistManager.resetAnimationState();

        sender.sendMessage("\u00a7aTablist config reloaded.");

        for (Player player : Bukkit.getOnlinePlayers()) {
            TablistManager.updateTablist(player);
        }

        return true;
    }
}
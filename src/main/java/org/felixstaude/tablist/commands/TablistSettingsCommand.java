package org.felixstaude.tablist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.felixstaude.tablist.core.PersonalTablistManager;

import java.util.*;

public class TablistSettingsCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
            "setheader", "setfooter", "clear", "enable", "disable", "toggle",
            "animation", "random", "interval"
    );

    private static final List<String> TRUE_FALSE = Arrays.asList("true", "false");
    private static final List<String> INTERVAL_TYPES = Arrays.asList("header", "footer");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("tablist.personal")) {
            player.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /tablist <setheader|setfooter|clear|enable|disable|toggle|animation|random|interval>");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "setheader":
            case "setfooter": {
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /tablist " + sub + " <line> <text>");
                    return true;
                }

                int line;
                try {
                    line = Integer.parseInt(args[1]) - 1; // 1-based for users
                    if (line < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage("§cLine must be a number starting from 1.");
                    return true;
                }

                String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                if (sub.equals("setheader")) {
                    PersonalTablistManager.setLine(player, "header", line, text);
                    player.sendMessage("§aHeader line " + (line + 1) + " updated.");
                } else {
                    PersonalTablistManager.setLine(player, "footer", line, text);
                    player.sendMessage("§aFooter line " + (line + 1) + " updated.");
                }
                break;
            }

            case "clear": {
                PersonalTablistManager.clearTablist(player);
                player.sendMessage("§aYour personal tablist has been cleared.");
                break;
            }

            case "enable": {
                PersonalTablistManager.setSetting(player, "use_personal_tablist", true);
                player.sendMessage("§aYour personal tablist is now enabled.");
                break;
            }

            case "disable": {
                PersonalTablistManager.setSetting(player, "use_personal_tablist", false);
                player.sendMessage("§cYour personal tablist is now disabled.");
                break;
            }

            case "toggle": {
                boolean current = PersonalTablistManager.getBooleanSetting(player, "use_personal_tablist");
                boolean next = !current;
                PersonalTablistManager.setSetting(player, "use_personal_tablist", next);
                player.sendMessage("§aYour personal tablist has been " + (next ? "enabled" : "disabled") + ".");
                break;
            }

            case "animation": {
                boolean enabled = Boolean.parseBoolean(args[1]);
                PersonalTablistManager.setSetting(player, "animation", enabled);
                player.sendMessage("§aAnimation setting updated to: " + enabled);
                break;
            }

            case "random": {
                boolean enabled = Boolean.parseBoolean(args[1]);
                PersonalTablistManager.setSetting(player, "random_order", enabled);
                player.sendMessage("§aRandom order setting updated to: " + enabled);
                break;
            }

            case "interval": {
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /tablist interval <header|footer> <ticks>");
                    return true;
                }
                String type = args[1].toLowerCase();
                int ticks;
                try {
                    ticks = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cTicks must be a number.");
                    return true;
                }

                if (!type.equals("header") && !type.equals("footer")) {
                    player.sendMessage("§cType must be 'header' or 'footer'");
                    return true;
                }

                String key = type.equals("header") ? "header_interval" : "footer_interval";
                PersonalTablistManager.setSetting(player, key, ticks);
                player.sendMessage("§aSet " + key.replace("_", " ") + " to " + ticks + " ticks.");
                break;
            }

            default:
                player.sendMessage("§cUnknown subcommand: " + sub);
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            return partialMatch(args[0], SUBCOMMANDS);
        }

        String sub = args[0].toLowerCase();

        if (args.length == 2) {
            switch (sub) {
                case "setheader":
                case "setfooter":
                    return Collections.singletonList("<line>");
                case "animation":
                case "random":
                    return partialMatch(args[1], TRUE_FALSE);
                case "interval":
                    return partialMatch(args[1], INTERVAL_TYPES);
            }
        }

        if (args.length == 3 && sub.equals("interval")) {
            return Collections.singletonList("<ticks>");
        }

        return Collections.emptyList();
    }

    private List<String> partialMatch(String input, List<String> options) {
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(input.toLowerCase())) {
                result.add(option);
            }
        }
        return result;
    }
}

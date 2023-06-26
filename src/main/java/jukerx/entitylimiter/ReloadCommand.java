package jukerx.entitylimiter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("entitylimiter.reload") || commandSender.hasPermission("entitylimiter.*")) {
                    EntityLimiter.getPlugin().reloadConfig();
                    commandSender.sendMessage("§aYou have reloaded the configuration file!");
                } else {
                    commandSender.sendMessage("§cYou don't have permission for this command.");
                }
            } else {
                if (commandSender.hasPermission("entitylimiter.reload") || commandSender.hasPermission("entitylimiter.*")) {
                    commandSender.sendMessage("§eUnrecognized argument, Try /el reload");
                } else {
                    commandSender.sendMessage("§cYou don't have permission for this command.");
                }
            }
        } else {
            if (commandSender.hasPermission("entitylimiter.reload") || commandSender.hasPermission("entitylimiter.*")) {
                commandSender.sendMessage("§eTry /el reload");
            } else {
                commandSender.sendMessage("§cYou don't have permission for this command.");
            }
        }
        return true;
    }
}

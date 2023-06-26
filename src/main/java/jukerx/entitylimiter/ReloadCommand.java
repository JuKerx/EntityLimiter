package jukerx.entitylimiter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("entitylimiter.reload") || commandSender.hasPermission("entitylimiter.*")) {
                    try {
                        EntityLimiter.getPlugin().reloadConfig();
                        commandSender.sendMessage("§aYou have reloaded the configuration file!");
                    } catch (Exception e){
                        commandSender.sendMessage("&cThere was a problem reloading the configuration file. Check console for errors!");
                        if (EntityLimiter.getPlugin().getConfig().getCurrentPath().isEmpty()) {
                            EntityLimiter.getPlugin().getConfig().options().copyDefaults();
                            EntityLimiter.getPlugin().saveConfig();
                        }
                    }
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
                commandSender.sendMessage("§aEntityLimiter §ev" + EntityLimiter.Version + " §aby jukerx");
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        if (strings.length > 0) {
            List<String> TabCompletes = new ArrayList<>();
            if (p.hasPermission("entitylimiter.reload")) {
                TabCompletes.add("reload");
            }
            return TabCompletes;
        }
        return null;
    }
}

package jukerx.entitylimiter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {
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

package jukerx.entitylimiter;


import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOError;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.yaml.snakeyaml.scanner.ScannerException;

public class Command implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (strings.length > 0) {
            if (!commandSender.hasPermission("entitylimiter.*")) {
                commandSender.sendMessage("§cYou don't have permission for this command.");
                return false;
            }
            if (strings[0].equalsIgnoreCase("reload")) {
                File config = new File(EntityLimiter.getPlugin().getDataFolder(), "config.yml");
                if (config.exists()) {
                    try {
                        EntityLimiter.getPlugin().reloadConfig();
                        commandSender.sendMessage("§aSuccessfully reloaded configuration file!");
                    } catch (ScannerException e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem reloading the configuration file. Check console for errors!");
                    }
                } else {
                    try {
                        EntityLimiter.getPlugin().getConfig().options().copyDefaults();
                        EntityLimiter.getPlugin().saveDefaultConfig();
                        commandSender.sendMessage("§aYour config.yml file was missing, Created a new one for you instead!");
                    } catch (IOError e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem creating a config file for you, Check console for errors!");
                    }
                }
            } else if (strings[0].equalsIgnoreCase("setradius")) {
                if (strings.length > 1) {
                    try {
                        EntityLimiter.getPlugin().getConfig().set("Radius", Double.valueOf(strings[1]));
                        EntityLimiter.getPlugin().saveConfig();
                        commandSender.sendMessage("§aLimit has been set to " + strings[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem configuring the radius. Check console for errors!");
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify an amount of radius!");
                }
            } else if (strings[0].equalsIgnoreCase("setheightradius")) {
                if (strings.length > 1) {
                    try {
                        EntityLimiter.getPlugin().getConfig().set("HeightRadius", Double.valueOf(strings[1]));
                        EntityLimiter.getPlugin().saveConfig();
                        commandSender.sendMessage("§aLimit radius has been set to " + strings[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem configuring the height radius. Check console for errors!");
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify an amount of height radius!");
                }
            } else if (strings[0].equalsIgnoreCase("setlimit")) {
                if (strings.length > 1) {
                    try {
                        EntityLimiter.getPlugin().getConfig().set("Limit", Integer.valueOf(strings[1]));
                        EntityLimiter.getPlugin().saveConfig();
                        commandSender.sendMessage("§aLimit has been set to " + strings[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem configuring the limit. Check console for errors!");
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify a limit!");
                }
            } else if (strings[0].equalsIgnoreCase("characterlimit")) {
                if (strings.length > 1) {
                    try {
                        EntityLimiter.getPlugin().getConfig().set("CharacterLimit", Integer.valueOf(strings[1]));
                        EntityLimiter.getPlugin().saveConfig();
                        commandSender.sendMessage("§aEntity name length limit has been set to " + strings[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem configuring the character limit. Check console for errors!");
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify a limit!");
                }
            } else if (strings[0].equalsIgnoreCase("particles")) {
                if (strings.length > 1) {
                    try {
                        EntityLimiter.getPlugin().getConfig().set("DisplayParticles", Boolean.valueOf(strings[1]));
                        EntityLimiter.getPlugin().saveConfig();
                        commandSender.sendMessage("§aParticle displays are now set to " + Boolean.valueOf(strings[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem configuring the limit. Check console for errors!");
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify a boolean!");
                }
            } else if (strings[0].equalsIgnoreCase("portals")) {
                if (strings.length > 1) {
                    try {
                        EntityLimiter.getPlugin().getConfig().set("EntityPortalEnter", Boolean.valueOf(strings[1]));
                        EntityLimiter.getPlugin().saveConfig();
                        commandSender.sendMessage("§aEntity portal events are now set to " + Boolean.valueOf(strings[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem configuring the limit. Check console for errors!");
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify a boolean!");
                }
            } else if (strings[0].equalsIgnoreCase("exemptworlds")) {
                if (strings.length > 1) {
                    EntityLimiter.getPlugin().reloadConfig();
                    if (!EntityLimiter.getPlugin().getConfig().getStringList("ExemptWorlds").contains(strings[1])) {
                        try {
                            if (Bukkit.getWorld(strings[1]) != null) {
                                List<String> i = new ArrayList<>(EntityLimiter.getPlugin().getConfig().getStringList("ExemptWorlds"));
                                i.add(Bukkit.getWorld(strings[1]).getName());
                                EntityLimiter.getPlugin().getConfig().set("ExemptWorlds", i);
                                EntityLimiter.getPlugin().saveConfig();
                                commandSender.sendMessage("§aAdded " + Bukkit.getWorld(strings[1]).getName() + " to the exempt worlds list!");
                            } else {
                                commandSender.sendMessage("§cThe world \"" + strings[1] + "\" does not exist!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commandSender.sendMessage("§cThere was a problem configuring the exempts. Check console for errors!");
                        }
                    } else {
                        try {
                            List<String> i = new ArrayList<>(EntityLimiter.getPlugin().getConfig().getStringList("ExemptWorlds"));
                            i.remove(strings[1]);
                            EntityLimiter.getPlugin().getConfig().set("ExemptWorlds", i);
                            EntityLimiter.getPlugin().saveConfig();
                            commandSender.sendMessage("§cRemoved " + strings[1] + " to the exempt worlds list!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            commandSender.sendMessage("§cThere was a problem configuring the exempts. Check console for errors!");
                        }
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify a world!");
                }
            } else if (strings[0].equalsIgnoreCase("exemptentities")) {
                if (strings.length > 1) {
                    EntityLimiter.getPlugin().reloadConfig();
                    if (!EntityLimiter.getPlugin().getConfig().getStringList("ExemptEntities").contains(strings[1])) {
                        try {
                            if (EntityType.valueOf(strings[1].toUpperCase()) != null) {
                                List<String> i = new ArrayList<>(EntityLimiter.getPlugin().getConfig().getStringList("ExemptEntities"));
                                i.add(EntityType.valueOf(strings[1]).name());
                                EntityLimiter.getPlugin().getConfig().set("ExemptEntities", i);
                                EntityLimiter.getPlugin().saveConfig();
                                commandSender.sendMessage("§aAdded " + EntityType.valueOf(strings[1]).name() + " to the exempt entities list!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commandSender.sendMessage("§cThe entity \"" + strings[1] + "\" does not exist!");
                        }
                    } else {
                        try {
                            List<String> i = new ArrayList<>(EntityLimiter.getPlugin().getConfig().getStringList("ExemptEntities"));
                            i.remove(strings[1]);
                            EntityLimiter.getPlugin().getConfig().set("ExemptEntities", i);
                            EntityLimiter.getPlugin().saveConfig();
                            commandSender.sendMessage("§eRemoved " + strings[1] + " to the exempt entities list!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            commandSender.sendMessage("§cThere was a problem configuring the exempts. Check console for errors!");
                        }
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify an entity!");
                }
            } else if (strings[0].equalsIgnoreCase("spawncap")) {
                if (strings.length > 1) {
                    try {
                        EntityLimiter.getPlugin().getConfig().set("SpawnCap", Boolean.valueOf(strings[1]));
                        EntityLimiter.getPlugin().saveConfig();
                        commandSender.sendMessage("§aEntity spawn cap is now set to " + Boolean.valueOf(strings[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                        commandSender.sendMessage("§cThere was a problem configuring the limit. Check console for errors!");
                    }
                } else {
                    commandSender.sendMessage("§cPlease specify a boolean!");
                }
            } else {
                commandSender.sendMessage("§cUnrecognized argument, Try /el (option)");
            }
        } else {
            commandSender.sendMessage("§aEntityLimiter §ev" + EntityLimiter.Version + " §aby jukerx");
        }
        return true;
    }

    final ImmutableList<String> TabCompletesList = ImmutableList.of("reload", "setradius", "setheightradius", "setlimit", "characterlimit", "particles", "portals", "exemptworlds", "exemptentities", "spawncap");

    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        switch (strings.length) {
            case 1:
                if (commandSender.hasPermission("entitylimiter.*"))
                    return EntityLimiter.tabComplete(strings[0], TabCompletesList);
            case 2:
                if (strings[0].equalsIgnoreCase("setradius"))
                    return Collections.singletonList(String.valueOf(EntityLimiter.getPlugin().getConfig().getInt("Radius")));
                if (strings[0].equalsIgnoreCase("setheightradius"))
                    return Collections.singletonList(String.valueOf(EntityLimiter.getPlugin().getConfig().getInt("HeightRadius")));
                if (strings[0].equalsIgnoreCase("setlimit"))
                    return Collections.singletonList(String.valueOf(EntityLimiter.getPlugin().getConfig().getInt("Limit")));
                if (strings[0].equalsIgnoreCase("characterlimit"))
                    return Collections.singletonList(String.valueOf(EntityLimiter.getPlugin().getConfig().getInt("CharacterLimit")));
                if (strings[0].equalsIgnoreCase("particles"))
                    return EntityLimiter.tabComplete(strings[1], "true", "false");
                if (strings[0].equalsIgnoreCase("portals"))
                    return EntityLimiter.tabComplete(strings[1], "true", "false");
                if (strings[0].equalsIgnoreCase("exemptworlds"))
                    return EntityLimiter.tabComplete(strings[1], EntityLimiter.WorldsList());
                if (strings[0].equalsIgnoreCase("exemptentities"))
                    return EntityLimiter.tabComplete(strings[1], EntityLimiter.EntityList());
                if (strings[0].equalsIgnoreCase("spawncap"))
                    return EntityLimiter.tabComplete(strings[1], "true", "false");
                break;
        }
        return Collections.emptyList();
    }
}

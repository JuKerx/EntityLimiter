package jukerx.entitylimiter;

import com.google.common.collect.Lists;
import org.bstats.charts.SimplePie;
import org.bstats.bukkit.Metrics;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;

public final class EntityLimiter extends JavaPlugin implements Listener {
    static EntityLimiter plugin;
    static double Version = 1.5;

    public void onEnable() {
        int pluginId = 18970;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new SimplePie("display_particles", () -> String.valueOf(getConfig().getBoolean("DisplayParticles"))));

        getCommand("entitylimiter").setExecutor(new Command());
        getCommand("entitylimiter").setTabCompleter(new Command());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        plugin = this;
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    // Very messy code that I'll redo later, this was one of my first plugins lol (Whatever works, works)

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Entity entity = e.getEntity();
        int Entities = 0;

        if (getConfig().getDouble("Radius") > 0) {
            for (Entity ent : entity.getWorld().getNearbyEntities(entity.getLocation(), getConfig().getDouble("Radius"), getConfig().getDouble("HeightRadius"), getConfig().getDouble("Radius"))) {
                if ( !(ent instanceof Player) && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {
                    Entities++;
                }
                if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                    continue;
                }
                if (!(ent instanceof Player) && ent.getPassengers().isEmpty() && Entities >= getConfig().getInt("Limit")) {
                    if (!getConfig().getBoolean("SpawnCap")) {
                        if (getConfig().getBoolean("DisplayParticles")) {
                            ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                        }
                        ent.remove();
                    } else {
                        e.setCancelled(true);
                        if (Entities > getConfig().getInt("Limit")) {
                            ent.remove();
                        }
                    }
                }
            }
        } else {
            for (Entity ent : entity.getWorld().getEntities()) {
                if ( !(ent instanceof Player) && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {
                    Entities++;
                }
                if (!(ent instanceof Player) && ent.getPassengers().isEmpty() && Entities >= getConfig().getInt("Limit")) {
                    if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                        continue;
                    }
                    if (!getConfig().getBoolean("SpawnCap")) {
                        if (getConfig().getBoolean("DisplayParticles")) {
                            ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                        }
                        ent.remove();
                    } else {
                        e.setCancelled(true);
                        if (Entities > getConfig().getInt("Limit")) {
                            ent.remove();
                        }
                    }
                }
            }
        }
        if (getConfig().getInt("CharacterLimit") > 0 && entity.getCustomName() != null && entity.getCustomName().length() >= getConfig().getInt("CharacterLimit")) {
            entity.setCustomName(colorize(getConfig().getString("LimitReplacementText")));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNametag(PlayerInteractEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Entity entity = e.getRightClicked();
        Player p = e.getPlayer();
        if (p.getItemInHand().getType() == Material.NAME_TAG && getConfig().getInt("CharacterLimit") > 0) {
            if (entity.getCustomName() != null && entity.getCustomName().length() >= getConfig().getInt("CharacterLimit")) {
                entity.setCustomName(colorize(getConfig().getString("LimitReplacementText")));
                e.setCancelled(true);
            } else if (entity.getCustomName() == null && p.getItemInHand().getItemMeta().getDisplayName().length() >= getConfig().getInt("CharacterLimit")) {
                entity.setCustomName(colorize(getConfig().getString("LimitReplacementText")));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleSpawn(VehicleCreateEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Vehicle entity = e.getVehicle();
        int Entities = 0;
        if (getConfig().getDouble("Radius") > 0) {
            for (Entity ent : entity.getWorld().getNearbyEntities(entity.getLocation(), getConfig().getDouble("Radius"), getConfig().getDouble("HeightRadius"), getConfig().getDouble("Radius"))) {
                if ( !(ent instanceof Player) && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {
                    Entities++;
                }
                if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                    continue;
                }
                if ( !(ent instanceof Player) && ent.getPassengers().isEmpty() && Entities >= getConfig().getInt("Limit")) {
                    if (!getConfig().getBoolean("SpawnCap")) {
                        if (getConfig().getBoolean("DisplayParticles")) {
                            ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                        }
                        ent.remove();
                    } else {
                        e.setCancelled(true);
                        if (Entities > getConfig().getInt("Limit")) {
                            ent.remove();
                        }
                    }
                }
            }
        } else {
            for (Entity ent : entity.getWorld().getEntities()) {
                if ( !(ent instanceof Player) && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {
                    Entities++;
                }
                if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                    continue;
                }
                if (!(ent instanceof Player) && ent.getPassengers().isEmpty() && Entities >= getConfig().getInt("Limit")) {
                    if (!getConfig().getBoolean("SpawnCap")) {
                        if (getConfig().getBoolean("DisplayParticles")) {
                            ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                        }
                        ent.remove();
                    } else {
                        e.setCancelled(true);
                        if (Entities > getConfig().getInt("Limit")) {
                            ent.remove();
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityPortalEnter(EntityPortalEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            return;
        }
        if (!getConfig().getBoolean("EntityPortalEnter")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPortalExit(EntityPortalExitEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Entity entity = e.getEntity();
        if (getConfig().getDouble("Radius") > 0) {
            for (Entity ent : entity.getWorld().getNearbyEntities(entity.getLocation(), getConfig().getDouble("Radius"), getConfig().getDouble("HeightRadius"), getConfig().getDouble("Radius")) ) {
                if ( !(ent instanceof Player) && ent.getPassengers().isEmpty() && ent.getWorld().getNearbyEntities(ent.getLocation(), getConfig().getDouble("Radius"), getConfig().getDouble("HeightRadius"), getConfig().getDouble("Radius")).size() >= getConfig().getInt("Limit") && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {

                    if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                        continue;
                    }

                    if (!getConfig().getBoolean("SpawnCap")) {
                        if (getConfig().getBoolean("DisplayParticles")) {
                            ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                        }
                        ent.remove();
                    } else {
                        e.setCancelled(true);
                        if (ent.getWorld().getNearbyEntities(ent.getLocation(), getConfig().getDouble("Radius"), getConfig().getDouble("HeightRadius"), getConfig().getDouble("Radius")).size() > getConfig().getInt("Limit")) {
                            ent.remove();
                        }
                    }
                }
            }
        } else {
            for (Entity ent : entity.getWorld().getEntities()) {
                if ( !(ent instanceof Player) && ent.getPassengers().isEmpty() && ent.getWorld().getEntities().size() >= getConfig().getInt("Limit") && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {
                    if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                        continue;
                    }
                    if (!getConfig().getBoolean("SpawnCap")) {
                        if (getConfig().getBoolean("DisplayParticles")) {
                            ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                        }
                        ent.remove();
                    } else {
                        e.setCancelled(true);
                        if (entity.getWorld().getEntities().size() > getConfig().getInt("Limit")) {
                            ent.remove();
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityGateway(EntityTeleportEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Entity entity = e.getEntity();
        if (e.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (getConfig().getBoolean("EntityPortalEnter")) {
                if (getConfig().getDouble("Radius") > 0) {
                    for (Entity ent : entity.getWorld().getNearbyEntities(entity.getLocation(), getConfig().getDouble("Radius"), getConfig().getDouble("HeightRadius"), getConfig().getDouble("Radius"))) {
                        if ( !(ent instanceof Player) && ent.getPassengers().isEmpty() && ent.getWorld().getNearbyEntities(ent.getLocation(), getConfig().getDouble("Radius"), getConfig().getDouble("HeightRadius"), getConfig().getDouble("Radius")).size() >= getConfig().getInt("Limit") && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {

                            if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                                continue;
                            }

                            if (!getConfig().getBoolean("SpawnCap")) {
                                if (getConfig().getBoolean("DisplayParticles")) {
                                    ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                                }
                                ent.remove();
                            } else {
                                e.setCancelled(true);
                                if (entity.getWorld().getEntities().size() > getConfig().getInt("Limit")) {
                                    ent.remove();
                                }
                            }
                        }
                    }
                } else {
                    for (Entity ent : entity.getWorld().getEntities()) {
                        if ( !(ent instanceof Player) && ent.getPassengers().isEmpty() && entity.getWorld().getEntities().size() >= getConfig().getInt("Limit") && !getConfig().getStringList("ExemptWorlds").contains(entity.getWorld().getName()) || !getConfig().getStringList("ExemptEntities").contains(ent.getType().name())) {

                            if (getConfig().getStringList("ExemptEntities").contains(ent.getType().name()) || getConfig().getStringList("ExemptWorlds").contains(ent.getWorld().getName())) {
                                continue;
                            }

                            if (!getConfig().getBoolean("SpawnCap")) {
                                if (getConfig().getBoolean("DisplayParticles")) {
                                    ent.getWorld().spawnParticle(Particle.valueOf(getConfig().getString("ParticleSettings.particle").toUpperCase()), ent.getLocation(), getConfig().getInt("ParticleSettings.amount"), getConfig().getDouble("ParticleSettings.offsetX"), getConfig().getDouble("ParticleSettings.offsetY"), getConfig().getDouble("ParticleSettings.offsetZ"), getConfig().getDouble("ParticleSettings.extra"));
                                }
                                ent.remove();
                            } else {
                                e.setCancelled(true);
                                if (entity.getWorld().getEntities().size() > getConfig().getInt("Limit")) {
                                    ent.remove();
                                }
                            }
                        }
                    }
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    public static List<String> WorldsList() {
        final Server server = Bukkit.getServer();
        final List<String> WorldList = Lists.newArrayList();
        for (final World w : server.getWorlds()) {
            WorldList.add(w.getName());
        }
        return WorldList;
    }

    public static List<String> EntityList() {
        final List<String> EntityList = Lists.newArrayList();
        for (final EntityType type : EntityType.values()) {
            EntityList.add(type.name());
        }
        return EntityList;
    }

    public static List<String> tabComplete(String t, List<?> s) {
        List<String> ret = new ArrayList<>();
        List<String> strs = new ArrayList<>();
        for (Object a : s) strs.add(a.toString());
        for (String b : strs) if (b.toLowerCase().startsWith(t.toLowerCase())) ret.add(b);
        return ret;
    }

    public static List<String> tabComplete(String t, String... s) {
        List<String> ret = new ArrayList<>();
        List<String> strs = new ArrayList<>();
        for (Object a : s) strs.add(a.toString());
        for (String b : strs) if (b.toLowerCase().startsWith(t.toLowerCase())) ret.add(b);
        return ret;
    }

    public static EntityLimiter getPlugin() {
        return plugin;
    }
    public static String colorize(String message) {
        return translateAlternateColorCodes('&', message);
    }
}

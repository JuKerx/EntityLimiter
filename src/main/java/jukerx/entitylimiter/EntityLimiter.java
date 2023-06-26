package jukerx.entitylimiter;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class EntityLimiter extends JavaPlugin implements Listener {

    private static EntityLimiter plugin;

    @Override
    public void onEnable() {
        getCommand("entitylimiter").setExecutor(new ReloadCommand());
        getCommand("entitylimiter").setTabCompleter(new TabCompletion());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        plugin = this;
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        int Entities = 0;
        for (Entity ent : entity.getWorld().getNearbyEntities(entity.getLocation(), getConfig().getDouble("Radius"), 500, getConfig().getDouble("Radius"))) {
            if (!(ent instanceof Player)) {
                Entities += 1;
                if (Entities >= getConfig().getInt("Limit")) {
                    if (getConfig().getBoolean("DisplayParticles")) {
                        ent.getWorld().spawnParticle(Particle.SMOKE_NORMAL, ent.getLocation(), 25, 0, 0.5, 0, 0.01);
                    }
                    ent.remove();
                }
            }
        }
    }

    @EventHandler
    public void onVehicleSpawn(VehicleCreateEvent e) {
        Vehicle entity = e.getVehicle();
        int Entities = 0;
        for (Entity ent : entity.getWorld().getNearbyEntities(entity.getLocation(), getConfig().getDouble("Radius"), 500, getConfig().getDouble("Radius"))) {
            if ((!(ent instanceof Player)) && (ent instanceof Vehicle)) {
                Entities += 1;
                if (Entities >= getConfig().getInt("Limit")) {
                    if (getConfig().getBoolean("DisplayParticles")) {
                        ent.getWorld().spawnParticle(Particle.SMOKE_NORMAL, ent.getLocation(), 25, 0, 0.5, 0, 0.01);
                    }
                    ent.remove();
                }
            }
        }
    }

    public static EntityLimiter getPlugin() {
        return plugin;
    }
}

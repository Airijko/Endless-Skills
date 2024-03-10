package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.XPConfiguration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityEventListener implements Listener {

    private final XPConfiguration xpConfiguration;
    private final LevelingManager levelingManager;

    public EntityEventListener(XPConfiguration xpConfiguration, LevelingManager levelingManager) {
        this.xpConfiguration = xpConfiguration;
        this.levelingManager = levelingManager;
    }

    // New EntityDeathEvent handler for mob kills
    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        EntityDamageEvent lastDamageCause = entity.getLastDamageCause();

        if (lastDamageCause instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) lastDamageCause;

            if (damageByEntityEvent.getDamager() instanceof Player) {
                Player player = (Player) damageByEntityEvent.getDamager();

                // Get the mob's name
                String mobName = entity.getType().name();

                // Use the getXPForMob method from XPConfiguration to get the XP value for the mob
                int xpForMob = xpConfiguration.getXPForMob(mobName);

                // Use the addXP method from LevelingManager to add XP and handle level-ups
                levelingManager.handleXP(player, xpForMob);
            }
        }
    }
}

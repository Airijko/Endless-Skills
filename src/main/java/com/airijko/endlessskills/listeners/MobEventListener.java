package com.airijko.endlessskills.listeners;

import com.airijko.endlessskills.leveling.LevelingManager;
import com.airijko.endlessskills.leveling.XPConfiguration;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobEventListener implements Listener {
    private final XPConfiguration xpConfiguration;
    private final LevelingManager levelingManager;

    public MobEventListener(XPConfiguration xpConfiguration, LevelingManager levelingManager) {
        this.xpConfiguration = xpConfiguration;
        this.levelingManager = levelingManager;
    }
    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        // Check if the entity was killed by an arrow
        if (killer instanceof Arrow) {
            Arrow arrow = (Arrow) killer;

            // Check if the arrow was shot by a player
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                handleEntityDeath(player, entity);
            }
        }

        // Check if the entity was killed by a player
        else if (killer != null) {
            handleEntityDeath(killer, entity);
        }
    }

    private void handleEntityDeath(Player player, LivingEntity entity) {
        // Get the mob's name
        String mobName = entity.getType().name();

        // Use the getXPForMob method from XPConfiguration to get the XP value for the mob
        double xpForMob = xpConfiguration.getXPForMob(mobName);

        // Use the handleXP method from LevelingManager to add XP and handle level-ups
        levelingManager.handleXP(player, xpForMob, false);
    }
}

package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.managers.PlayerDataManager;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerEventListener implements Listener {

    private final PlayerDataManager playerDataManager;
    private final XPConfiguration xpConfiguration;
    private final LevelingManager levelingManager;

    public PlayerEventListener(PlayerDataManager playerDataManager, XPConfiguration xpConfiguration, LevelingManager levelingManager) {
        this.playerDataManager = playerDataManager;
        this.xpConfiguration = xpConfiguration;
        this.levelingManager = levelingManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        playerDataManager.getPlayerDataFile(playerUUID);
    }

    // New EntityDeathEvent handler for mob kills
    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        EntityDamageEvent lastDamageCause = entity.getLastDamageCause();

        // Check if the last damage cause is an instance of EntityDamageByEntityEvent
        if (lastDamageCause instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) lastDamageCause;

            if (damageByEntityEvent.getDamager() instanceof Player) {
                Player player = (Player) damageByEntityEvent.getDamager();

                // Use the addXP method from LevelingManager to add XP and handle level-ups
                int xpForMob = xpConfiguration.getMobKillXP(); // Use the XP value from XPConfiguration
                levelingManager.addXP(player, xpForMob);
            }
        }
    }




}

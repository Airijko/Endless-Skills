package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.managers.PlayerDataManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Entity;
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
        File playerDataFile = playerDataManager.getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

        // Set the player's name, UUID, initial values for Mob Kill Count, XP, and Level
        playerDataConfig.set("UUID", playerUUID.toString());
        playerDataConfig.set("Name", player.getName());
        playerDataConfig.set("Mob Kill Count", 0);
        playerDataConfig.set("XP", 0);
        playerDataConfig.set("Level", 1);

        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            player.getServer().getLogger().log(Level.SEVERE, "Failed to save player data", e);
        }
    }

    // New EntityDeathEvent handler for mob kills
    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        EntityDamageByEntityEvent lastDamageCause = (EntityDamageByEntityEvent) entity.getLastDamageCause();

        if (lastDamageCause != null && lastDamageCause.getDamager() instanceof Player) {
            Player player = (Player) lastDamageCause.getDamager();

            // Use the addXP method from LevelingManager to add XP and handle level-ups
            int xpForMob = xpConfiguration.getMobKillXP(); // Use the XP value from XPConfiguration
            levelingManager.addXP(player, xpForMob);
        }
    }



}

package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.managers.ConfigManager;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.Set;

public class ExperienceTracker implements Listener {
    private final ConfigManager configManager;
    private final XPConfiguration xpConfiguration;
    private final LevelingManager levelingManager;
    private final Set<Location> playerPlacedBlocks;

    public ExperienceTracker(ConfigManager configManager, XPConfiguration xpConfiguration, LevelingManager levelingManager) {
        this.configManager = configManager;
        this.xpConfiguration = xpConfiguration;
        this.levelingManager = levelingManager;
        this.playerPlacedBlocks = new HashSet<>();
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
                double xpForMob = xpConfiguration.getXPForMob(mobName);

                // Use the addXP method from LevelingManager to add XP and handle level-ups
                levelingManager.handleXP(player, xpForMob, true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // When a block is placed, add its location to the set
        playerPlacedBlocks.add(event.getBlock().getLocation());
    }

    @EventHandler
    public void onBlockMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();

        // If the block was placed by a player, don't award XP
        if (playerPlacedBlocks.contains(blockLocation)) {
            playerPlacedBlocks.remove(blockLocation);
            return;
        }

        boolean gainXPFromBlocks = configManager.getConfig().getBoolean("gain_xp_from_blocks", true);
        if (!gainXPFromBlocks) {
            return; // If not, do not award the XP and return
        }

        // Get the block's name
        String blockName = event.getBlock().getType().name();

        // Use the getXPForBlock method from XPConfiguration to get the XP value for the block
        double xpForBlock = xpConfiguration.getXPForBlock(blockName);

        // Use the handleXP method from LevelingManager to add XP and handle level-ups
        levelingManager.handleXP(player, xpForBlock, true);
    }
}

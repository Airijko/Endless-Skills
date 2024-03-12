package com.airijko.endlessskills.leveling;

import com.airijko.endlessskills.managers.ConfigManager;
import com.airijko.endlessskills.managers.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LevelingManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final PlayerDataManager playerDataManager;
    private final LevelConfiguration levelConfiguration;
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final Map<UUID, BukkitTask> removalTasks = new HashMap<>();

    public LevelingManager(JavaPlugin plugin, ConfigManager configManager, PlayerDataManager playerDataManager, LevelConfiguration levelConfiguration) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playerDataManager = playerDataManager;
        this.levelConfiguration = levelConfiguration;
    }

    private void displayLevelUpMessage(Player player, int newLevel) {
        int skillPointsToAdd = levelConfiguration.getSkillPointsPerLevel();
        int currentSkillPoints = playerDataManager.getPlayerSkillPoints(player.getUniqueId());

        // Create a title
        Title title = Title.title(
                Component.text("Leveled Up!", NamedTextColor.GREEN, TextDecoration.BOLD), // Title
                Component.text("+" + skillPointsToAdd + " SP!", NamedTextColor.AQUA), // Subtitle
                Title.Times.of(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1)) // Times
        );

        // Display the title
        player.showTitle(title);

        // Send the skill points gained message
        player.sendMessage(Component.text("You have " + currentSkillPoints + " skill points available!", NamedTextColor.AQUA, TextDecoration.BOLD));

        // Broadcast the level up announcement
        Bukkit.broadcast(Component.text(player.getName(), NamedTextColor.GOLD, TextDecoration.BOLD)
                .append(Component.text(" has leveled up to ", NamedTextColor.YELLOW))
                .append(Component.text("Level " + newLevel, NamedTextColor.GOLD, TextDecoration.BOLD)));
    }

    public boolean playerLevelUp(Player player) {
        UUID playerUUID = player.getUniqueId();
        double currentXP = playerDataManager.getPlayerXP(playerUUID);
        int currentLevel = playerDataManager.getPlayerLevel(playerUUID);
        double xpForNextLevel = levelConfiguration.calculateThreshold(currentLevel);

        // Check if the player has enough XP to level up
        if (currentXP >= xpForNextLevel) {
            int newLevel = currentLevel + 1;
            playerDataManager.setPlayerLevel(playerUUID, newLevel);

            // Calculate the excess XP
            double excessXP = (currentXP - xpForNextLevel);

            // Retrieve the skill points to add from the configuration
            int skillPointsToAdd = levelConfiguration.getSkillPointsPerLevel();

            // Update the player's skill points
            int currentSkillPoints = playerDataManager.getPlayerSkillPoints(playerUUID);
            playerDataManager.setPlayerSkillPoints(playerUUID, currentSkillPoints + skillPointsToAdd);

            // Reset the player's XP to 0 and add the excess XP
            playerDataManager.setPlayerXP(playerUUID, excessXP);

            // Display the level-up message
            displayLevelUpMessage(player, newLevel);

            return true; // Indicate that the player has leveled up
        }

        return false; // Indicate that the player has not leveled up
    }

    public void changePlayerLevel(UUID playerUUID, int newLevel) {
        // Reset the player's data
        playerDataManager.resetPlayerData(playerUUID);

        // Set the player's level
        playerDataManager.setPlayerLevel(playerUUID, newLevel);

        // Set the player's skill points based on the new level and the skillPointsPerLevel value
        int skillPointsPerLevel = levelConfiguration.getSkillPointsPerLevel();
        int totalSkillPoints = newLevel * skillPointsPerLevel;
        playerDataManager.setPlayerSkillPoints(playerUUID, totalSkillPoints);
    }

    private void displayXPGainedMessage(Player player, double newXP, double xpThresholdForNextLevel) {
        UUID playerUUID = player.getUniqueId();
        String formattedXP = String.format("%.2f", newXP);
        Component bossBarTitleComponent = Component.text(formattedXP + " / " + (int) xpThresholdForNextLevel + " XP", NamedTextColor.GREEN);
        String bossBarTitle = LegacyComponentSerializer.legacySection().serialize(bossBarTitleComponent);

        // Check if a boss bar already exists for the player
        BossBar bossBar = playerBossBars.get(playerUUID);
        BukkitTask removalTask = removalTasks.get(playerUUID);

        if (bossBar == null) {
            // Create a new boss bar if it doesn't exist
            bossBar = Bukkit.createBossBar(
                    bossBarTitle,
                    BarColor.GREEN, // Color
                    BarStyle.SOLID // Style
            );
            playerBossBars.put(playerUUID, bossBar);
        } else {
            // Update the title of the existing boss bar
            bossBar.setTitle(bossBarTitle);
        }

        // Calculate the progress
        double progress = newXP / xpThresholdForNextLevel;
        bossBar.setProgress(progress);

        // Add the player to the boss bar if not already added
        if (!bossBar.getPlayers().contains(player)) {
            bossBar.addPlayer(player);
        }

        // Cancel the existing removal task if it exists
        if (removalTask != null) {
            removalTask.cancel();
        }

        // Schedule a new removal task
        BossBar finalBossBar = bossBar;
        removalTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (finalBossBar.getPlayers().contains(player)) {
                finalBossBar.removePlayer(player);
            }
            playerBossBars.remove(playerUUID); // Remove the boss bar from the map
            removalTasks.remove(playerUUID); // Remove the removal task from the map
        }, 60L); // 60 ticks = 3 seconds

        // Store the new removal task
        removalTasks.put(playerUUID, removalTask);
    }

    public void handleXP(Player player, double XPGain, boolean isFromBlock) {
        UUID playerUUID = player.getUniqueId();
        boolean gainXPFromBlocks = configManager.getConfig().getBoolean("gain_xp_from_blocks", true);
        double currentXP = playerDataManager.getPlayerXP(playerUUID);
        double newXP = currentXP + XPGain;

        if (isFromBlock && !gainXPFromBlocks) {
            return; // If so, do not award the XP and return
        }

        // Update the player's XP
        playerDataManager.setPlayerXP(playerUUID, newXP);

        // Check for level-up
        boolean hasLeveledUp = playerLevelUp(player);

        // If the player has not leveled up, display the XP gained message
        if (!hasLeveledUp) {
            // Calculate the current level and the threshold for the next level
            int currentLevel = playerDataManager.getPlayerLevel(playerUUID);
            double xpThresholdForNextLevel = levelConfiguration.calculateThreshold(currentLevel);

            // Display the XP gained message
            displayXPGainedMessage(player, newXP, xpThresholdForNextLevel);
        }
    }
}

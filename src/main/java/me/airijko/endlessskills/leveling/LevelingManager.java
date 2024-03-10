package me.airijko.endlessskills.leveling;

import me.airijko.endlessskills.managers.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

import org.bukkit.Bukkit;

public class LevelingManager {

    private final PlayerDataManager playerDataManager;
    private final LevelConfiguration levelConfiguration;

    public LevelingManager(PlayerDataManager playerDataManager, LevelConfiguration levelConfiguration) {
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
        int currentXP = playerDataManager.getPlayerXP(playerUUID);
        int currentLevel = playerDataManager.getPlayerLevel(playerUUID);
        double xpForNextLevel = levelConfiguration.calculateThreshold(currentLevel);

        // Check if the player has enough XP to level up
        if (currentXP >= xpForNextLevel) {
            int newLevel = currentLevel + 1;
            playerDataManager.setPlayerLevel(playerUUID, newLevel);

            // Calculate the excess XP
            int excessXP = (int) (currentXP - xpForNextLevel);

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

    private void displayXPGainedMessage(Player player, int newXP, double xpThresholdForNextLevel) {
        player.sendMessage(Component.text("Current XP: " + newXP + " / " + (int) xpThresholdForNextLevel, NamedTextColor.GREEN));
    }

    public void addXP(Player player, int xpToAdd) {
        UUID playerUUID = player.getUniqueId();
        int currentXP = playerDataManager.getPlayerXP(playerUUID);
        int newXP = currentXP + xpToAdd;

        // Update the player's XP
        playerDataManager.setPlayerXP(playerUUID, newXP);

        // Check for level-up
        boolean hasLeveledUp = playerLevelUp(player);

        // If the player has leveled up, do not send the XP gained message
        if (!hasLeveledUp) {
            // Calculate the current level and the threshold for the next level
            int currentLevel = playerDataManager.getPlayerLevel(playerUUID);
            double xpThresholdForNextLevel = levelConfiguration.calculateThreshold(currentLevel);

            // Correctly call the displayXPGainedMessage method with matching parameters
            displayXPGainedMessage(player, newXP, xpThresholdForNextLevel);
        }
    }
}

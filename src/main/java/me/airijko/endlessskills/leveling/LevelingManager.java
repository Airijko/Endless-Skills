package me.airijko.endlessskills.leveling;

import me.airijko.endlessskills.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LevelingManager {

    private final PlayerDataManager playerDataManager;
    private final LevelThresholdCalculator levelThresholdCalculator;

    public LevelingManager(PlayerDataManager playerDataManager, LevelThresholdCalculator levelThresholdCalculator) {
        this.playerDataManager = playerDataManager;
        this.levelThresholdCalculator = levelThresholdCalculator;
    }

    private void displayLevelUpMessage(Player player, int newLevel) {
        int nextLevel = newLevel + 1;
        double correctNextLevelThreshold = levelThresholdCalculator.calculateThreshold(nextLevel);

        player.sendMessage(ChatColor.GREEN + "Congratulations! You have leveled up to " + ChatColor.BOLD + "Level " + newLevel + ChatColor.RESET + ". You need " + (int)correctNextLevelThreshold + " XP towards your next level.");
        Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.YELLOW + " has leveled up to " + ChatColor.BOLD + "Level " + newLevel + ChatColor.RESET + "!");
    }

    public boolean checkForLevelUp(Player player) {
        UUID playerUUID = player.getUniqueId();
        int currentXP = playerDataManager.getPlayerXP(playerUUID);
        int currentLevel = playerDataManager.getPlayerLevel(playerUUID);
        double xpForNextLevel = levelThresholdCalculator.calculateThreshold(currentLevel + 1);

        // Check if the player has enough XP to level up
        if (currentXP >= xpForNextLevel) {
            int newLevel = currentLevel + 1;
            playerDataManager.setPlayerLevel(playerUUID, newLevel);

            // Calculate the excess XP
            int excessXP = (int) (currentXP - xpForNextLevel);

            // Reset the player's XP to 0 and add the excess XP
            playerDataManager.setPlayerXP(playerUUID, excessXP);

            // Display the level-up message
            displayLevelUpMessage(player, newLevel);

            return true; // Indicate that the player has leveled up
        }

        return false; // Indicate that the player has not leveled up
    }

    private void displayXPGainedMessage(Player player, int newXP, double xpThresholdForNextLevel) {
        player.sendMessage(ChatColor.GREEN + "Current XP: "  + newXP + " / " + (int)xpThresholdForNextLevel);
    }

    public void addXP(Player player, int xpToAdd) {
        UUID playerUUID = player.getUniqueId();
        int currentXP = playerDataManager.getPlayerXP(playerUUID);
        int newXP = currentXP + xpToAdd;

        // Update the player's XP
        playerDataManager.setPlayerXP(playerUUID, newXP);

        // Check for level-up
        boolean hasLeveledUp = checkForLevelUp(player);

        // If the player has leveled up, do not send the XP gained message
        if (!hasLeveledUp) {
            // Calculate the current level and the threshold for the next level
            int currentLevel = playerDataManager.getPlayerLevel(playerUUID);
            double xpThresholdForNextLevel = levelThresholdCalculator.calculateThreshold(currentLevel + 1);

            // Correctly call the displayXPGainedMessage method with matching parameters
            displayXPGainedMessage(player, newXP, xpThresholdForNextLevel);
        }
    }
}

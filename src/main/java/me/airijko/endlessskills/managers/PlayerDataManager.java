package me.airijko.endlessskills.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataManager {

    private final JavaPlugin plugin;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public File getPlayerDataFile(UUID playerUUID) {
        File playerDataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!playerDataFolder.exists()) {
            // Ignore the return value by not assigning it to a variable
            playerDataFolder.mkdir();
        }
        File playerDataFile = new File(playerDataFolder, playerUUID.toString() + ".yml");
        if (!playerDataFile.exists()) {
            try {
                // Ignore the return value by not assigning it to a variable
                playerDataFile.createNewFile();
                YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
                playerDataConfig.set("UUID", playerUUID.toString());
                Player player = Bukkit.getPlayer(playerUUID);
                String playerName = player != null ? player.getName() : "Unknown Player"; // Add an empty string for player name
                playerDataConfig.set("PlayerName", playerName);
                playerDataConfig.set("Mob Kill Count", 0);
                playerDataConfig.set("XP", 0);
                playerDataConfig.set("Level", 1);
                playerDataConfig.save(playerDataFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save player data", e);
            }
        }
        return playerDataFile;
    }

    // Method to get the player's level
    public int getPlayerLevel(UUID playerUUID) {
        File playerDataFile = getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        return playerDataConfig.getInt("Level", 1); // Default to 1 if "Level" is not set
    }

    public int getPlayerXP(UUID playerUUID) {
        File playerDataFile = getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        return playerDataConfig.getInt("XP", 0);
    }

    public void setPlayerLevel(UUID playerUUID, int level) {
        File playerDataFile = getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        playerDataConfig.set("Level", level);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save player data", e);
        }
    }

    public void setPlayerXP(UUID playerUUID, int xp) {
        File playerDataFile = getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        playerDataConfig.set("XP", xp);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save player data", e);
        }
    }
}

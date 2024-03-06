package me.airijko.endlessskills;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataManager {

    private static final String DATA_FOLDER_NAME = "playerdata";
    private final JavaPlugin plugin;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void incrementMobKillCount(Player player) {
        UUID playerUUID = player.getUniqueId();
        Path playerDataFilePath = getPlayerDataFilePath(playerUUID);
        YamlConfiguration playerDataConfig = new YamlConfiguration();

        try {
            if (!Files.exists(playerDataFilePath)) {
                Files.createDirectories(playerDataFilePath.getParent());
                Files.createFile(playerDataFilePath);
            }

            // Load the existing data or create a new configuration
            playerDataConfig.load(playerDataFilePath.toFile());

            // Set the player's UUID and name
            playerDataConfig.set("uuid", playerUUID.toString());
            playerDataConfig.set("name", player.getName());

            // Increment and set the mob kill count
            int currentCount = playerDataConfig.getInt("mobKillCount", 0);
            playerDataConfig.set("mobKillCount", currentCount + 1);

            // Save the updated configuration
            playerDataConfig.save(playerDataFilePath.toFile());

            // Log the player's name and their updated mob kill count
            plugin.getLogger().log(Level.INFO, player.getName() + " has killed a mob. Their mob kill count is now: " + (currentCount + 1));
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to update mob kill count for player " + player.getName(), e);
        }
    }

    private Path getPlayerDataFilePath(UUID playerUUID) {
        return Paths.get(plugin.getDataFolder().getPath(), "playerdata", playerUUID.toString() + ".yml");
    }

}

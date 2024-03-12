package com.airijko.endlessskills.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private Map<String, Double> weaponStrengthValues;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadWeaponStrengthValues();
    }

    private void loadWeaponStrengthValues() {
        weaponStrengthValues = new HashMap<>();
        try {
            for (String key : Objects.requireNonNull(config.getConfigurationSection("weapon_strength_value")).getKeys(false)) {
                weaponStrengthValues.put(key, config.getDouble("weapon_strength_value." + key, 0.0));
            }
        } catch (NullPointerException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load weapon strength values from config.yml. The weapon_strength_value section is missing or commented out.");
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    public double getWeaponStrengthValue(String weaponType) {
        return weaponStrengthValues.getOrDefault(weaponType, 0.1);
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    }
}
package me.airijko.endlessskills.leveling;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;

public class LevelConfiguration {

    private final JavaPlugin plugin;
    private double multiplier;
    private double base;
    private int skillPointsPerLevel;

    public LevelConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
        loadLevelingConfiguration();
    }

    public void loadLevelingConfiguration() {
        File levelingFile = new File(plugin.getDataFolder(), "leveling.yml");
        if (!levelingFile.exists()) {
            try (InputStream in = plugin.getResource("leveling.yml")) {
                if (in == null) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to load leveling.yml from resources.");
                    return;
                }
                Files.copy(in, levelingFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create leveling.yml file.", e);
                return;
            }
        }

        YamlConfiguration levelingFormulaConfig = YamlConfiguration.loadConfiguration(levelingFile);
        this.multiplier = levelingFormulaConfig.getDouble("default.multiplier", 100.0);
        this.base = levelingFormulaConfig.getDouble("default.base", 100.0);
        this.skillPointsPerLevel = levelingFormulaConfig.getInt("skillPointsPerLevel", 5);
    }

    public double calculateThreshold(int level) {
        // Calculate the threshold using the formula
        double threshold = multiplier * Math.pow(level - 2, 2) + base;
        return threshold;
    }

    public int getSkillPointsPerLevel() {
        return skillPointsPerLevel;
    }
}

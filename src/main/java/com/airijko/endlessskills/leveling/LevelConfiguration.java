package com.airijko.endlessskills.leveling;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class LevelConfiguration {

    private final JavaPlugin plugin;
    private String expression;
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
                plugin.getLogger().log(Level.SEVERE, "Failed to load leveling.yml from resources.", e);
            }
        }

        YamlConfiguration levelingConfig = YamlConfiguration.loadConfiguration(levelingFile);
        this.expression = levelingConfig.getString("default.expression", "base * ((log(level)+1) * (sqrt(level)))^2");
        this.base = levelingConfig.getDouble("default.base", 100.0);
        this.skillPointsPerLevel = levelingConfig.getInt("skillPointsPerLevel", 3);
    }

    public double calculateThreshold(int level) {
        // Parse the expression
        Expression e = new ExpressionBuilder(expression)
                .variables("level", "base")
                .build()
                .setVariable("level", level)
                .setVariable("base", base);

        // Evaluate the expression
        return e.evaluate();
    }

    public int getSkillPointsPerLevel() {
        return skillPointsPerLevel;
    }
}

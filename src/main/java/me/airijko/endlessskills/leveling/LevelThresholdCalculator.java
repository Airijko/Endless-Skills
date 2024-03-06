package me.airijko.endlessskills.leveling;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.logging.Level;
import java.nio.file.Files;
import java.io.IOException;

public class LevelThresholdCalculator {

    private final JavaPlugin plugin;
    private double multiplier;
    private double base;

    public LevelThresholdCalculator(JavaPlugin plugin) {
        this.plugin = plugin;
        loadLevelingFormula();
    }

    public void loadLevelingFormula() {
        File levelingFormulaFile = new File(plugin.getDataFolder(), "leveling_formula.yml");
        if (!levelingFormulaFile.exists()) {
            try {
                if (!levelingFormulaFile.createNewFile()) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to create leveling_formula.yml file.");
                } else {
                    // Write the default formula to the file
                    String defaultFormula = "default:\n" +
                            " expression: 'multiplier * (level - 2) ^ 2 + base'\n" +
                            " multiplier: 100.0\n" +
                            " base: 100.0";
                    Files.write(levelingFormulaFile.toPath(), defaultFormula.getBytes());
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to generate leveling_formula.yml file.", e);
            }
        }

        YamlConfiguration levelingFormulaConfig = YamlConfiguration.loadConfiguration(levelingFormulaFile);
        this.multiplier = levelingFormulaConfig.getDouble("default.multiplier", 100.0);
        this.base = levelingFormulaConfig.getDouble("default.base", 100.0);
    }

    public double calculateThreshold(int level) {
        // Calculate the threshold using the formula
        double threshold = multiplier * Math.pow(level - 2, 2) + base;
        return threshold;
    }
}

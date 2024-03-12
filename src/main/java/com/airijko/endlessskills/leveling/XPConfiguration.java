package com.airijko.endlessskills.leveling;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;

public class XPConfiguration {

    private final JavaPlugin plugin;
    private final Map<String, Double> mobXPMap;
    private final Map<String, Double> blockXPMap;
    private final double fallbackXP;

    public XPConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mobXPMap = new HashMap<>();
        this.blockXPMap = new HashMap<>();
        this.fallbackXP = 0.1;
        loadXPConfiguration();
    }

    public void loadXPConfiguration() {
        File xpSourcesFile = new File(plugin.getDataFolder(), "xp_sources.yml");
        if (!xpSourcesFile.exists()) {
            try (InputStream in = plugin.getResource("xp_sources.yml")) {
                if (in == null) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to load xp_sources.yml from resources.");
                    return;
                }
                Files.copy(in, xpSourcesFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create xp_sources.yml file.", e);
                return;
            }
        }

        YamlConfiguration xpSourcesConfig = YamlConfiguration.loadConfiguration(xpSourcesFile);
        // Load the mobs from the 'mobs' section into the mobXPMap
        ConfigurationSection mobsSection = xpSourcesConfig.getConfigurationSection("mobs");
        if (mobsSection != null) {
            for (String mobName : mobsSection.getKeys(false)) {
                mobXPMap.put(mobName, mobsSection.getDouble(mobName));
            }
        } else {
            plugin.getLogger().log(Level.SEVERE, "Failed to load mobs from xp_sources.yml.");
        }

        // Load the blocks from the 'blocks' section into the blockXPMap
        ConfigurationSection blocksSection = xpSourcesConfig.getConfigurationSection("blocks");
        if (blocksSection != null) {
            for (String blockName : blocksSection.getKeys(false)) {
                blockXPMap.put(blockName, blocksSection.getDouble(blockName));
            }
        } else {
            plugin.getLogger().log(Level.SEVERE, "Failed to load blocks from xp_sources.yml.");
        }
    }

    public double getXPForMob(String mobName) {
        // Return the XP for the given mob, or the fallback value if the mob is not found
        return mobXPMap.getOrDefault(mobName, fallbackXP);
    }

    public double getXPForBlock(String blockName) {
        // Return the XP for the given block, or the fallback value if the block is not found
        return blockXPMap.getOrDefault(blockName, fallbackXP);
    }
}
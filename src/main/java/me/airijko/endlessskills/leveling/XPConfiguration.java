package me.airijko.endlessskills.leveling;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;

public class XPConfiguration {

    private final JavaPlugin plugin;
    private final Map<String, Integer> mobXPMap;
    private final int fallbackXP;

    public XPConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mobXPMap = new HashMap<>();
        this.fallbackXP = 1;
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
                mobXPMap.put(mobName, mobsSection.getInt(mobName));
            }
        } else {
            plugin.getLogger().log(Level.SEVERE, "Failed to load mobs from xp_sources.yml.");
        }
    }

    public int getXPForMob(String mobName) {
        // Return the XP for the given mob, or the fallback value if the mob is not found
        return mobXPMap.getOrDefault(mobName, fallbackXP);
    }
}

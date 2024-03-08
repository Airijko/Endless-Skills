package me.airijko.endlessskills.leveling;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class XPConfiguration {

    private final JavaPlugin plugin;
    private int mobKillXP;
    private int blockBrokenXP;

    public XPConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
        loadXPConfiguration();
    }

    public void loadXPConfiguration() {
        File xpSourcesFile = new File(plugin.getDataFolder(), "xp_sources.yml");
        if (!xpSourcesFile.exists()) {
            try {
                if (!xpSourcesFile.createNewFile()) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to create the xp_sources.yml file.");
                } else {
                    // Write the default experience points to the xp_sources.yml file
                    String defaultSources = "experience_per_mob_kill: 5\nexperience_per_block_broken: 1";
                    Files.write(xpSourcesFile.toPath(), defaultSources.getBytes());
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create or write to the xp_sources.yml file.", e);
            }
        }

        YamlConfiguration xpSourcesConfig = YamlConfiguration.loadConfiguration(xpSourcesFile);
        this.mobKillXP = xpSourcesConfig.getInt("experience_per_mob_kill", 5); // Default to 5 if not specified
        this.blockBrokenXP = xpSourcesConfig.getInt("experience_per_block_broken", 1); // Default to 1 if not specified
    }

    public int getMobKillXP() {
        return mobKillXP;
    }

    public int getBlockBrokenXP() {
        return blockBrokenXP;
    }
}

package me.airijko.endlessskills;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public final class EndlessSkills extends JavaPlugin {

    @Override
    public void onEnable() {
        // Directly create the "playerdata" folder within the plugin's data folder
        File playerDataFolder = new File(getDataFolder(), "playerdata");
        if (!playerDataFolder.exists() && !playerDataFolder.mkdirs()) {
            getLogger().log(Level.SEVERE, "Failed to create the playerdata folder.");
            return; // Exit the method if the folder cannot be created
        }

        // Register your event listeners here
        getServer().getPluginManager().registerEvents(new MobKillListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package me.airijko.endlessskills;

import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.listeners.PlayerEventListener;
import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.LevelThresholdCalculator;
import me.airijko.endlessskills.commands.ReloadCommand;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

        PlayerDataManager playerDataManager = new PlayerDataManager(this);
        XPConfiguration xpConfiguration = new XPConfiguration(this);
        LevelThresholdCalculator levelThresholdCalculator = new LevelThresholdCalculator(this);
        LevelingManager levelUpManager = new LevelingManager(playerDataManager, levelThresholdCalculator);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(playerDataManager, xpConfiguration, levelUpManager), this);

        // Reload Command
        this.getCommand("endless").setExecutor(new ReloadCommand(this, xpConfiguration, levelThresholdCalculator));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

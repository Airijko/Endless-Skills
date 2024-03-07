package me.airijko.endlessskills;

import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.listeners.PlayerEventListener;
import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.LevelConfiguration;
import me.airijko.endlessskills.commands.ReloadCommand;
import me.airijko.endlessskills.commands.EndlessCommand;
import me.airijko.endlessskills.gui.EndlessSkillsGUI;

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

        PlayerDataManager playerDataManager = new PlayerDataManager(this);
        EndlessSkillsGUI gui = new EndlessSkillsGUI(playerDataManager, this);
        XPConfiguration xpConfiguration = new XPConfiguration(this);
        LevelConfiguration levelConfiguration = new LevelConfiguration(this);
        ReloadCommand reloadCommand = new ReloadCommand(this, xpConfiguration, levelConfiguration);
        EndlessCommand command = new EndlessCommand(gui, reloadCommand);

        getCommand("endless").setExecutor(command);

        LevelingManager levelUpManager = new LevelingManager(playerDataManager, levelConfiguration);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(playerDataManager, xpConfiguration, levelUpManager), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

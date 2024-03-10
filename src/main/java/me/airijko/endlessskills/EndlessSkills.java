package me.airijko.endlessskills;

import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.LevelConfiguration;
import me.airijko.endlessskills.commands.ResetAttributesCommand;
import me.airijko.endlessskills.commands.ReloadCommand;
import me.airijko.endlessskills.commands.EndlessCommand;
import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.skills.SkillAttributes;

import me.airijko.endlessskills.listeners.PlayerEventListener;
import me.airijko.endlessskills.listeners.EntityEventListener;
import me.airijko.endlessskills.listeners.EndlessGUIListener;
import me.airijko.endlessskills.listeners.DamageListener;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EndlessSkills extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save the default config.yml file if it doesn't exist
        saveDefaultConfig();

        PlayerDataManager playerDataManager = new PlayerDataManager(this);
        LevelConfiguration levelConfiguration = new LevelConfiguration(this);
        SkillAttributes skillAttributes = new SkillAttributes(this, playerDataManager);
        PlayerEventListener playerEventListener = new PlayerEventListener(playerDataManager);
        EndlessSkillsGUI endlessSkillsGUI = new EndlessSkillsGUI(playerDataManager, skillAttributes);
        LevelingManager levelingManager = new LevelingManager(playerDataManager, levelConfiguration);
        XPConfiguration xpConfiguration = new XPConfiguration(this);
        ReloadCommand reloadCommand = new ReloadCommand(xpConfiguration, levelConfiguration);
        ResetAttributesCommand resetAttributesCommand = new ResetAttributesCommand();
        EndlessCommand endlessCommand = new EndlessCommand(this, endlessSkillsGUI, reloadCommand, resetAttributesCommand, playerDataManager, levelingManager);

        levelConfiguration.loadLevelingConfiguration();
        playerDataManager.loadPlayerDataFolder();
        skillAttributes.applyModifiersToAllPlayers();
        endlessCommand.registerCommands();

        getServer().getPluginManager().registerEvents(playerEventListener, this);
        getServer().getPluginManager().registerEvents(new EntityEventListener(xpConfiguration, levelingManager), this);
        getServer().getPluginManager().registerEvents(new DamageListener(skillAttributes, this), this);
        getServer().getPluginManager().registerEvents(new EndlessGUIListener(endlessSkillsGUI, skillAttributes), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

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

import me.airijko.endlessskills.listeners.EntityEventListener;
import me.airijko.endlessskills.listeners.AttributeListener;
import me.airijko.endlessskills.listeners.EndlessGUIListener;

import org.bukkit.plugin.java.JavaPlugin;

public final class EndlessSkills extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save the default config.yml file if it doesn't exist
        saveDefaultConfig();

        PlayerDataManager playerDataManager = new PlayerDataManager(this);
        LevelConfiguration levelConfiguration = new LevelConfiguration(this);
        SkillAttributes skillAttributes = new SkillAttributes(this, playerDataManager);
        AttributeListener attributeListener = new AttributeListener(skillAttributes);
        LevelingManager levelingManager = new LevelingManager(playerDataManager, levelConfiguration);
        XPConfiguration xpConfiguration = new XPConfiguration(this);
        EndlessSkillsGUI gui = new EndlessSkillsGUI(playerDataManager);
        ReloadCommand reloadCommand = new ReloadCommand(this, xpConfiguration, levelConfiguration);
        ResetAttributesCommand resetAttributesCommand = new ResetAttributesCommand();
        EndlessCommand command = new EndlessCommand(gui, reloadCommand, resetAttributesCommand);

        levelConfiguration.loadLevelingConfiguration();
        playerDataManager.loadPlayerDataFolder();
        skillAttributes.applyModifiersToAllPlayers();
        getCommand("endless").setExecutor(command);
        getServer().getPluginManager().registerEvents(new EntityEventListener(xpConfiguration, levelingManager), this);
        getServer().getPluginManager().registerEvents(attributeListener, this);
        getServer().getPluginManager().registerEvents(new EndlessGUIListener(gui), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

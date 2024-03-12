package me.airijko.endlessskills;

import me.airijko.endlessskills.commands.LevelCommand;
import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.managers.ConfigManager;
import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.leveling.LevelConfiguration;
import me.airijko.endlessskills.commands.ResetAttributesCommand;
import me.airijko.endlessskills.commands.ReloadCommand;
import me.airijko.endlessskills.commands.EndlessCommand;
import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.skills.SkillAttributes;

import me.airijko.endlessskills.listeners.PlayerEventListener;
import me.airijko.endlessskills.listeners.ExperienceTracker;
import me.airijko.endlessskills.listeners.EndlessGUIListener;
import me.airijko.endlessskills.listeners.DamageListener;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EndlessSkills extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private ConfigManager configManager;
    private LevelConfiguration levelConfiguration;
    private SkillAttributes skillAttributes;
    private PlayerEventListener playerEventListener;
    private EndlessSkillsGUI endlessSkillsGUI;
    private LevelingManager levelingManager;
    private XPConfiguration xpConfiguration;
    private ReloadCommand reloadCommand;
    private ResetAttributesCommand resetAttributesCommand;
    private LevelCommand levelCommand;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        playerDataManager = new PlayerDataManager(this);
        levelConfiguration = new LevelConfiguration(this);
        skillAttributes = new SkillAttributes(this, configManager, playerDataManager);
        playerEventListener = new PlayerEventListener(playerDataManager);
        endlessSkillsGUI = new EndlessSkillsGUI(playerDataManager, skillAttributes);
        levelingManager = new LevelingManager(this, configManager, playerDataManager, levelConfiguration);
        xpConfiguration = new XPConfiguration(this);
        reloadCommand = new ReloadCommand(configManager, endlessSkillsGUI, xpConfiguration, levelConfiguration);
        levelCommand = new LevelCommand(playerDataManager, levelingManager);
        resetAttributesCommand = new ResetAttributesCommand();

        levelConfiguration.loadLevelingConfiguration();
        playerDataManager.loadPlayerDataFolder();
        skillAttributes.applyModifiersToAllPlayers();

        getServer().getPluginManager().registerEvents(playerEventListener, this);
        getServer().getPluginManager().registerEvents(new ExperienceTracker(configManager, xpConfiguration, levelingManager), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this, skillAttributes, configManager), this);
        getServer().getPluginManager().registerEvents(new EndlessGUIListener(endlessSkillsGUI, skillAttributes), this);

        Objects.requireNonNull(getCommand("endless")).setExecutor(new EndlessCommand(endlessSkillsGUI, reloadCommand, resetAttributesCommand, levelCommand));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (endlessSkillsGUI != null) {
            endlessSkillsGUI.closeForAllPlayers();
        }
    }
}
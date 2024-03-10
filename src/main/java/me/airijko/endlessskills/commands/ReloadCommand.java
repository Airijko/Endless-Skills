package me.airijko.endlessskills.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.airijko.endlessskills.leveling.XPConfiguration;
import me.airijko.endlessskills.leveling.LevelConfiguration;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final XPConfiguration xpConfiguration;
    private final LevelConfiguration levelConfiguration;

    public ReloadCommand(XPConfiguration xpConfiguration, LevelConfiguration levelConfiguration) {
        this.xpConfiguration = xpConfiguration;
        this.levelConfiguration = levelConfiguration;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("endless") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("endless.reload")) {
                // Reload the XP configuration
                xpConfiguration.loadXPConfiguration();
                // Reload the leveling formula configuration
                levelConfiguration.loadLevelingConfiguration();

                sender.sendMessage("EndlessSkills configuration has been reloaded!");
                return true;
            } else {
                sender.sendMessage("You do not have permission to use this command.");
                return false;
            }
        }
        return false;
    }
}


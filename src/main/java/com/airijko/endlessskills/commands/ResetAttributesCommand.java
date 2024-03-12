package com.airijko.endlessskills.commands;

import com.airijko.endlessskills.skills.SkillAttributes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class ResetAttributesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("This command can only be used in the console.");
            return true;
        }

        // Iterate over all online players
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            // Reset attributes to their default values
            SkillAttributes.resetAllAttributesToDefault(player);
            // Send a message to the console indicating the action
            sender.sendMessage("Attributes for " + player.getName() + " have been reset to their default values.");
        }

        return true;
    }
}
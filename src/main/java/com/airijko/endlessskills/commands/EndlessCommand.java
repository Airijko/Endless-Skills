package com.airijko.endlessskills.commands;

import com.airijko.endlessskills.gui.EndlessSkillsGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

import java.util.HashMap;

public class EndlessCommand implements CommandExecutor {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public EndlessCommand(EndlessSkillsGUI gui, ReloadCommand reloadCommand, ResetAttributesCommand resetAttributesCommand, LevelCommand levelCommand) {
        subCommands.put("reload", reloadCommand);
        subCommands.put("skills", new SkillsCommand(gui));
        subCommands.put("resetattributes", resetAttributesCommand);
        subCommands.put("level", levelCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            CommandExecutor executor = subCommands.get(subCommand);
            if (executor != null) {
                return executor.onCommand(sender, command, label, args);
            }
        }
        sender.sendMessage(Component.text("Usage: /endless [reload|skills|resetattributes|level]", NamedTextColor.RED));
        return false;
    }
}
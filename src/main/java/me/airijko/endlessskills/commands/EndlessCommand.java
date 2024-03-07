package me.airijko.endlessskills.commands;

import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.commands.ReloadCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class EndlessCommand implements CommandExecutor {

    private final EndlessSkillsGUI gui;
    private final ReloadCommand reloadCommand; // Reference to the ReloadCommand class

    public EndlessCommand(EndlessSkillsGUI gui, ReloadCommand reloadCommand) {
        this.gui = gui;
        this.reloadCommand = reloadCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String subCommand = args[0].toLowerCase();
                if (subCommand.equals("reload")) {
                    // Call the onCommand method of ReloadCommand
                    return reloadCommand.onCommand(sender, command, label, args);
                } else if (subCommand.equals("skills")) {
                    gui.openGUI(player);
                    return true;
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "Usage: /endless [reload|skills]");
        return false;
    }
}


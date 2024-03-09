package me.airijko.endlessskills.commands;

import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.managers.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class EndlessCommand implements CommandExecutor {

    private final EndlessSkillsGUI gui;
    private final ReloadCommand reloadCommand;
    private final ResetAttributesCommand resetAttributesCommand;
    private final LevelCommand levelCommand;

    public EndlessCommand(EndlessSkillsGUI gui, ReloadCommand reloadCommand, ResetAttributesCommand resetAttributesCommand, PlayerDataManager playerDataManager, LevelingManager levelingManager) {
        this.gui = gui;
        this.reloadCommand = reloadCommand;
        this.resetAttributesCommand = resetAttributesCommand;
        this.levelCommand = new LevelCommand(playerDataManager, levelingManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String subCommand = args[0].toLowerCase();
                if (subCommand.equals("reload")) {
                    return reloadCommand.onCommand(sender, command, label, args);
                } else if (subCommand.equals("skills")) {
                    gui.skillAttributesGUI(player, false);
                    return true;
                } else if (subCommand.equals("resetattributes")) {
                    return resetAttributesCommand.onCommand(sender, command, label, args);
                } else if (subCommand.equals("level")) {
                    return levelCommand.onCommand(sender, command, label, args);
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "Usage: /endless [reload|skills|resetattributes|resetplayer]");
        return false;
    }
}
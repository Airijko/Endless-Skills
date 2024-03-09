package me.airijko.endlessskills.commands;

import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.managers.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String subCommand = args[0].toLowerCase();
                switch (subCommand) {
                    case "reload":
                        return reloadCommand.onCommand(sender, command, label, args);
                    case "skills":
                        gui.skillAttributesGUI(player, false);
                        return true;
                    case "resetattributes":
                        return resetAttributesCommand.onCommand(sender, command, label, args);
                    case "level":
                        return levelCommand.onCommand(sender, command, label, args);
                    default:
                        sender.sendMessage(Component.text("Usage: /endless [reload|skills|resetattributes|resetplayer]", NamedTextColor.RED));
                        return false;
                }
            }
        }
        sender.sendMessage(Component.text("Usage: /endless [reload|skills|resetattributes|resetplayer]", NamedTextColor.RED));
        return false;
    }
}
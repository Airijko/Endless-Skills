package me.airijko.endlessskills.commands;

import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.managers.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LevelCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LevelingManager levelingManager;

    public LevelCommand(PlayerDataManager playerDataManager, LevelingManager levelingManager) {
        this.playerDataManager = playerDataManager;
        this.levelingManager = levelingManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length > 2) {
                    String action = args[1].toLowerCase();
                    String targetPlayerName = args[2];
                    Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
                    if (targetPlayer != null) {
                        handleAction(player, action, targetPlayer, args);
                    } else {
                        player.sendMessage("Player " + targetPlayerName + " not found.");
                    }
                } else {
                    player.sendMessage("Please specify a player name.");
                }
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
            return true;
        }
        return false;
    }

    private void handleAction(Player player, String action, Player targetPlayer, String[] args) {
        if (action.equals("set")) {
            if (args.length > 3) {
                try {
                    int newLevel = Integer.parseInt(args[3]);
                    levelingManager.changePlayerLevel(targetPlayer.getUniqueId(), newLevel);
                    player.sendMessage("Player level for " + targetPlayer.getName() + " has been set to " + newLevel + ".");
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid level. Please enter a valid number.");
                }
            } else {
                player.sendMessage("Please specify a level.");
            }
        } else if (action.equals("reset")) {
            playerDataManager.resetPlayerData(targetPlayer.getUniqueId());
            player.sendMessage("Player data for " + targetPlayer.getName() + " has been reset.");
        }
    }
}
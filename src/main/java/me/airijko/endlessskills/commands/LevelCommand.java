package me.airijko.endlessskills.commands;

import me.airijko.endlessskills.leveling.LevelingManager;
import me.airijko.endlessskills.managers.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LevelCommand implements CommandExecutor {

    private static final String SET_COMMAND = "set";
    private static final String RESET_COMMAND = "reset";

    private final PlayerDataManager playerDataManager;
    private final LevelingManager levelingManager;

    public LevelCommand(PlayerDataManager playerDataManager, LevelingManager levelingManager) {
        this.playerDataManager = playerDataManager;
        this.levelingManager = levelingManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        if (!player.isOp()) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length < 3) {
            player.sendMessage("Please specify a player name.");
            return true;
        }

        String action = args[1].toLowerCase();
        String targetPlayerName = args[2];
        Player targetPlayer = player.getServer().getPlayer(targetPlayerName);

        if (targetPlayer == null) {
            player.sendMessage("Player " + targetPlayerName + " not found.");
            return true;
        }

        switch (action) {
            case SET_COMMAND:
                handleSetCommand(player, targetPlayer, args);
                break;
            case RESET_COMMAND:
                handleResetCommand(player, targetPlayer);
                break;
            default:
                player.sendMessage("Invalid command. Use 'set' or 'reset'.");
                break;
        }

        return true;
    }

    private void handleSetCommand(Player player, Player targetPlayer, String[] args) {
        if (args.length < 4) {
            player.sendMessage("Please specify a level.");
            return;
        }

        try {
            int newLevel = Integer.parseInt(args[3]);
            levelingManager.changePlayerLevel(targetPlayer.getUniqueId(), newLevel);
            player.sendMessage("Player level for " + targetPlayer.getName() + " has been set to " + newLevel + ".");
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid level. Please enter a valid number.");
        }
    }

    private void handleResetCommand(Player player, Player targetPlayer) {
        playerDataManager.resetPlayerData(targetPlayer.getUniqueId());
        player.sendMessage("Player data for " + targetPlayer.getName() + " has been reset.");
    }
}
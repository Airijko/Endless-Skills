package com.airijko.endlessskills.listeners;

import com.airijko.endlessskills.managers.PlayerDataManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerEventListener implements Listener {
    private final PlayerDataManager playerDataManager;

    public PlayerEventListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        playerDataManager.getPlayerDataFile(player);
    }
}

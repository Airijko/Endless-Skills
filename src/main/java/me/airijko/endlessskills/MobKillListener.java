package me.airijko.endlessskills;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobKillListener implements Listener {

    private final PlayerDataManager playerDataManager;

    public MobKillListener(EndlessSkills plugin) {
        this.playerDataManager = new PlayerDataManager(plugin);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            playerDataManager.incrementMobKillCount(player);
        }
    }
}

package me.airijko.endlessskills.skills;

import org.bukkit.entity.Player;

public class SkillAttributes {

    public void modifyLifeForce(Player player) {
        player.sendMessage("You've chosen Life Force");
    }

    public void modifyStrength(Player player) {
        player.sendMessage("You've chosen Strength");
    }

    public void modifyTenacity(Player player) {
        player.sendMessage("You've chosen Tenacity");
    }

    public void modifyHaste(Player player) {
        player.sendMessage("You've chosen Haste");
    }

    public void modifyFocus(Player player) {
        player.sendMessage("You've chosen Focus");
    }
}

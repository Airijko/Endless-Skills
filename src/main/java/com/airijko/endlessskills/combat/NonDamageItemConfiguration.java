package com.airijko.endlessskills.combat;

import com.airijko.endlessskills.managers.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NonDamageItemConfiguration extends Weapon {
    public NonDamageItemConfiguration(ConfigManager configManager) {
        super(null, configManager); // Specific weapon type for this class
    }

    @Override
    public void modifyDamage(Player player, int level, EntityDamageByEntityEvent event) {
        double damageValue = event.getDamage();
        damageValue += getDamageModifier(player, level);
        event.setDamage(damageValue);
    }

    @Override
    protected double getDamageModifier(Player player, int level) {
        String weaponTypeName = "none";
        double strengthValue = configManager.getWeaponStrengthValue(weaponTypeName);
        return level * strengthValue; // strength value from config file
    }
}
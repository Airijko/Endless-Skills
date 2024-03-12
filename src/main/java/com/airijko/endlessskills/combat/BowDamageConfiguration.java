package com.airijko.endlessskills.combat;

import org.bukkit.Material;
import com.airijko.endlessskills.managers.ConfigManager;
import org.bukkit.entity.Player;

public class BowDamageConfiguration extends Weapon {
    public BowDamageConfiguration(ConfigManager configManager) {
        super(Material.BOW, configManager); // Specific weapon type for this class
    }

    @Override
    protected double getDamageModifier(Player player, int level) {
        String weaponTypeName = weaponType.name().toLowerCase();
        double strengthValue = configManager.getWeaponStrengthValue(weaponTypeName);
        return level * strengthValue; // strength value from config file
    }
}
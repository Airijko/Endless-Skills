package com.airijko.endlessskills.combat;

import com.airijko.endlessskills.managers.ConfigManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WeaponDamageConfiguration extends Weapon {
    public WeaponDamageConfiguration(Material weaponType, ConfigManager configManager) {
        super(weaponType, configManager); // Pass the specific weapon type to the superclass
    }

    @Override
    public void modifyDamage(Player player, int level, EntityDamageByEntityEvent event) {
        double damageValue = event.getDamage();
        damageValue += getDamageModifier(player, level);
        event.setDamage(damageValue);
    }

    @Override
    protected double getDamageModifier(Player player, int level) {
        String weaponTypeName = weaponType.name().toLowerCase();
        if (weaponTypeName.contains("_")) {
            weaponTypeName = weaponTypeName.substring(weaponTypeName.indexOf("_") + 1);
        }
        double strengthValue = configManager.getWeaponStrengthValue(weaponTypeName);
        return level * strengthValue; // strength value from config file
    }
}
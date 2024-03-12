package com.airijko.endlessskills.combat;

import com.airijko.endlessskills.managers.ConfigManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class Weapon {
    protected Material weaponType;
    protected ConfigManager configManager;

    public Weapon(Material weaponType, ConfigManager configManager) {
        this.weaponType = weaponType;
        this.configManager = configManager;
    }

    public void modifyDamage(Player player, int level, EntityDamageByEntityEvent event) {
        Material heldItem = player.getInventory().getItemInMainHand().getType();
        if (heldItem == weaponType) {
            double damageValue = event.getDamage();
            damageValue += getDamageModifier(player, level);
            event.setDamage(damageValue);
        }
    }

    protected double getDamageModifier(Player player, int level) {
        String weaponTypeName = weaponType == null ? "none" : weaponType.name().toLowerCase();
        double strengthValue = configManager.getWeaponStrengthValue(weaponTypeName);
        return level * strengthValue;
    }
}
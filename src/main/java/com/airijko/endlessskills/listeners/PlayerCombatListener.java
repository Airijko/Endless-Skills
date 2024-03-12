package com.airijko.endlessskills.listeners;

import com.airijko.endlessskills.combat.*;
import com.airijko.endlessskills.skills.SkillAttributes;
import com.airijko.endlessskills.managers.ConfigManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class PlayerCombatListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final SkillAttributes skillAttributes;
    private final HashMap<UUID, Boolean> eventProcessed;

    public PlayerCombatListener(JavaPlugin plugin, ConfigManager configManager, SkillAttributes skillAttributes) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.skillAttributes = skillAttributes;
        this.eventProcessed = new HashMap<>();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        // Check if the damager is an arrow
        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;

            // Check if the arrow was shot by a player
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                handleDamageEvent(player, event);
            }
        }

        // Check if the damager is a player
        else if (damager instanceof Player) {
            Player player = (Player) damager;
            handleDamageEvent(player, event);
        }
    }

    private void handleDamageEvent(Player player, EntityDamageByEntityEvent event) {
        UUID playerUUID = player.getUniqueId();

        if (eventProcessed.containsKey(playerUUID) && eventProcessed.get(playerUUID)) {
            return; // Ignore the event if it has already been processed
        }

        // Get the attribute levels
        int precisionLevel = skillAttributes.getAttributeLevel(playerUUID, "Precision");
        int strengthLevel = skillAttributes.getAttributeLevel(playerUUID, "Strength");

        Material weaponType = player.getInventory().getItemInMainHand().getType();
        Weapon weapon;

        if (weaponType == Material.BOW) {
            weapon = new BowDamageConfiguration(configManager);
        } else if (weaponType.name().endsWith("_SWORD") || weaponType.name().endsWith("_AXE") || weaponType.name().endsWith("TRIDENT")) {
            weapon = new WeaponDamageConfiguration(weaponType, configManager);
        } else {
            weapon = new NonDamageItemConfiguration(configManager);
        }

        // Apply the strength bonus to the damage
        weapon.modifyDamage(player, strengthLevel, event);

        // Apply the precision and ferocity bonuses to the damage
        boolean isCriticalHit = skillAttributes.handleCriticalHit(precisionLevel, event);

        // Format the damage value to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double damageValue = event.getDamage();
        damageValue = Double.parseDouble(decimalFormat.format(damageValue));

        // Apply the new damage value to the event
        event.setDamage(damageValue);

        // Display the damage hologram
        Location location = event.getEntity().getLocation(); // Use the entity's location from the event
        DamageHologram hologram = new DamageHologram(configManager, location, damageValue, isCriticalHit);
        Bukkit.getScheduler().runTaskLater(plugin, hologram::remove, 40L);

        // Mark the event as processed for this player
        eventProcessed.put(playerUUID, true);

        // Reset the flag after a short delay to allow for subsequent attacks
        Bukkit.getScheduler().runTaskLater(plugin, () -> eventProcessed.put(playerUUID, false), 1L);
    }
}
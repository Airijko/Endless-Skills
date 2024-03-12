package com.airijko.endlessskills.combat;

import com.airijko.endlessskills.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import java.util.Random;

public class DamageHologram {
    private final ArmorStand armorStand;
    private static final Random random = new Random();

    public DamageHologram(ConfigManager configManager, Location location, double damage, boolean isCritical) {
        boolean isEnabled = configManager.getConfig().getBoolean("damage_hologram_enabled", true);

        if (!isEnabled) {
            this.armorStand = null;
            return;
        }

        double offsetX = random.nextDouble() * 0.5 - 0.25; // Random value between -0.25 and 0.25
        double offsetY = random.nextDouble() * 0.5 - 0.25; // Random value between -0.25 and 0.25
        double offsetZ = random.nextDouble() * 0.5 - 0.25; // Random value between -0.25 and 0.25

        // Apply the random offset to the location
        Location offsetLocation = location.add(offsetX, offsetY, offsetZ);

        this.armorStand = (ArmorStand) offsetLocation.getWorld().spawnEntity(offsetLocation, EntityType.ARMOR_STAND);
        this.armorStand.setGravity(true);
        this.armorStand.setCanPickupItems(false);
        long roundedDamage = Math.round(damage);
        String formattedMessage = isCritical ? "§c§l\uD83D\uDCA5" + roundedDamage : "§7" + roundedDamage;
        // Convert the formattedMessage to a Component
        Component componentMessage = Component.text(formattedMessage);
        this.armorStand.customName(componentMessage);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setInvisible(true);
    }

    public void remove() {
        if (armorStand.isValid()) {
            armorStand.remove();
        }
    }
}
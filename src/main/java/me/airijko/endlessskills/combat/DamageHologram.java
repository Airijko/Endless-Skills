package me.airijko.endlessskills.combat;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class DamageHologram {
    private final ArmorStand armorStand;
    private static final Random random = new Random();

    public DamageHologram(JavaPlugin plugin, Location location, String message, boolean isCritical) {

        boolean isEnabled = plugin.getConfig().getBoolean("damage_hologram_enabled", true);

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
        String formattedMessage = isCritical ? "§c§l\uD83D\uDCA5" + message : "§7" + message;
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

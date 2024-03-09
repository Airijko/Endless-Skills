package me.airijko.endlessskills.combat;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;

public class DamageHologram {
    private final ArmorStand armorStand;
    private static final Random random = new Random();

    public DamageHologram(JavaPlugin plugin, Location location, String message, boolean isCritical) {
        // Define the range for the random offset. Adjust these values as needed.
        double offsetX = random.nextDouble() * 0.5 - 0.25; // Random value between -0.25 and 0.25
        double offsetY = random.nextDouble() * 0.5 - 0.25; // Random value between -0.25 and 0.25
        double offsetZ = random.nextDouble() * 0.5 - 0.25; // Random value between -0.25 and 0.25

        // Apply the random offset to the location
        Location offsetLocation = location.add(offsetX, offsetY, offsetZ);

        this.armorStand = (ArmorStand) offsetLocation.getWorld().spawnEntity(offsetLocation, EntityType.ARMOR_STAND);
        this.armorStand.setGravity(false);
        this.armorStand.setCanPickupItems(false);
        String formattedMessage = isCritical ? "§c§l\uD83D\uDCA5" + message : "§a" + message;
        this.armorStand.setCustomName(formattedMessage);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setInvisible(true);

        // Schedule a task to make the hologram fall slightly
        new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.setGravity(true);
            }
        }.runTaskLater(plugin, 1L); // 1 tick delay to ensure the hologram is spawned before applying gravity
    }

    public void remove() {
        if (armorStand.isValid()) {
            armorStand.remove();
        }
    }
}

package me.airijko.endlessskills.skills;

import me.airijko.endlessskills.managers.PlayerDataManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class SkillAttributes {

    private final JavaPlugin plugin;
    private final PlayerDataManager playerDataManager;

    public SkillAttributes(JavaPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    public void modifyLifeForce(Player player, int level) {
        double lifeForceValue = plugin.getConfig().getDouble("skill_attributes.life_force", 1.0) * level;
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(maxHealth.getBaseValue() + lifeForceValue);
        }
    }

    public void modifyStrength(Player player, int level) {
        double strengthValue = plugin.getConfig().getDouble("skill_attributes.strength", 0.5) * level;
        AttributeInstance attackDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.setBaseValue(attackDamage.getBaseValue() + strengthValue);
        }
    }

    public void modifyTenacity(Player player, int level) {
        double toughnessValue = plugin.getConfig().getDouble("skill_attributes.tenacity.toughness", 0.0125) * level;
        double knockbackResistanceValue = plugin.getConfig().getDouble("skill_attributes.tenacity.knock_back_resistance", 0.003) * level;
        AttributeInstance toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        AttributeInstance knockbackResistance = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (toughness != null) {
            toughness.setBaseValue(toughness.getBaseValue() + toughnessValue);
        }
        if (knockbackResistance != null) {
            knockbackResistance.setBaseValue(knockbackResistance.getBaseValue() + knockbackResistanceValue);
        }
    }

    public void modifyHaste(Player player, int level) {
        double attackSpeedValue = plugin.getConfig().getDouble("skill_attributes.haste.attack_speed", 0.02) * level;
        double movementSpeedValue = plugin.getConfig().getDouble("skill_attributes.haste.movement_speed", 0.001) * level;
        AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        AttributeInstance movementSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attackSpeed != null) {
            attackSpeed.setBaseValue(attackSpeed.getBaseValue() + attackSpeedValue);
        }
        if (movementSpeed != null) {
            movementSpeed.setBaseValue(movementSpeed.getBaseValue() + movementSpeedValue);
        }
    }

    public double modifyPrecision(int level) {
        double precisionValue = plugin.getConfig().getDouble("skill_attributes.precision.critical_rate", 0.75) * level;
        return precisionValue / 100;
    }

    public double modifyFerocity(int level) {
        double ferocityValue = plugin.getConfig().getDouble("skill_attributes.ferocity.critical_damage", 1.5) * level;
        return ferocityValue  / 100;
    }


    public void applyModifierToPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();

        // Reset all attributes to default for the specific player
        resetAllAttributesToDefault(player);

        // Apply Life Force modifier
        int lifeForceLevel = getAttributeLevel(playerUUID, "Life_Force");
        modifyLifeForce(player, lifeForceLevel);

        // Apply Strength modifier
        int strengthLevel = getAttributeLevel(playerUUID, "Strength");
        modifyStrength(player, strengthLevel);

        // Apply Tenacity modifier
        int tenacityLevel = getAttributeLevel(playerUUID, "Tenacity");
        modifyTenacity(player, tenacityLevel);

        // Apply Haste modifier
        int hasteLevel = getAttributeLevel(playerUUID, "Haste");
        modifyHaste(player, hasteLevel);

        // Apply Precision modifier
        int precisionLevel = getAttributeLevel(playerUUID, "Precision");
        modifyPrecision(precisionLevel);

        int ferocityLevel = getAttributeLevel(playerUUID, "Ferocity");
        modifyPrecision(ferocityLevel);
    }

    public void applyModifiersToAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            applyModifierToPlayer(player);
        }
    }

    private static void resetAttribute(Player player, Attribute attribute, double value) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(value);
        }
    }
    public static void resetAllAttributesToDefault(Player player) {
        resetAttribute(player, Attribute.GENERIC_MAX_HEALTH, 20.0); // Default max health
        resetAttribute(player, Attribute.GENERIC_ATTACK_DAMAGE, 2.0); // Default attack damage
        resetAttribute(player, Attribute.GENERIC_ARMOR_TOUGHNESS, 0.0); // Default armor toughness
        resetAttribute(player, Attribute.GENERIC_KNOCKBACK_RESISTANCE, 0.0); // Default knockback resistance
        resetAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.1); // Default movement speed
        resetAttribute(player, Attribute.GENERIC_ATTACK_SPEED, 4.0); // Default attack speed
    }

    public void useSkillPoint(UUID playerUUID, String attributeName) {
        // Retrieve the current skill points for the player
        int currentSkillPoints = playerDataManager.getPlayerSkillPoints(playerUUID);

        // Check if the player has enough skill points to level up the attribute
        if (currentSkillPoints > 0) {
            // Subtract one skill point
            playerDataManager.setPlayerSkillPoints(playerUUID, currentSkillPoints - 1);

            // Increase the attribute level by 1
            int currentAttributeLevel = getAttributeLevel(playerUUID, attributeName);
            setAttributeLevel(playerUUID, attributeName, currentAttributeLevel + 1);

            // Optionally, send a message to the player indicating the attribute level has increased
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(Component.text("Leveled ", NamedTextColor.GREEN)
                        .append(Component.text(attributeName, NamedTextColor.AQUA))
                        .append(Component.text(" to ", NamedTextColor.GREEN))
                        .append(Component.text(String.valueOf(currentAttributeLevel + 1), NamedTextColor.AQUA))
                        .append(Component.text("!", NamedTextColor.GREEN)));
                applyModifierToPlayer(player);
            }
        } else {
            // Optionally, send a message to the player indicating they don't have enough skill points
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(Component.text("Not enough skill points to level up ", NamedTextColor.RED)
                        .append(Component.text(attributeName, NamedTextColor.AQUA))
                        .append(Component.text(".", NamedTextColor.RED)));
            }
        }
    }

    // Method to get the level of a specific attribute
    public int getAttributeLevel(UUID playerUUID, String attributeName) {
        File playerDataFile = playerDataManager.getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        return playerDataConfig.getInt("Attributes." + attributeName, 1); // Default to 1 if level is not set
    }

    // Method to set the level of a specific attribute
    public void setAttributeLevel(UUID playerUUID, String attributeName, int level) {
        File playerDataFile = playerDataManager.getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        playerDataConfig.set("Attributes." + attributeName, level);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save player data", e);
        }
    }

    public String getAttributeDescription(String attributeName) {
        switch (attributeName) {
            case "Life_Force":
                return "Increases max health by 1.0 per level.";
            case "Strength":
                return "Increases attack damage by 0.5 per level.";
            case "Tenacity":
                return "Increases armor toughness by 0.0125 and knockback resistance by 0.003 per level.";
            case "Haste":
                return "Increases attack speed by 0.04 and movement speed by 0.01 per level.";
            case "Precision":
                return "Increase critical rate by 0.75% per level.";
            case "Ferocity":
                return "Increase critical damage by 1.5% per level.";
            default:
                return "Description not found.";
        }
    }
}

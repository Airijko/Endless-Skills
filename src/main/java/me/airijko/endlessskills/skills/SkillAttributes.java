package me.airijko.endlessskills.skills;

import me.airijko.endlessskills.managers.ConfigManager;
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
import java.util.ArrayList;
import java.util.List;

public class SkillAttributes {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final PlayerDataManager playerDataManager;

    public SkillAttributes(JavaPlugin plugin, ConfigManager configManager, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playerDataManager = playerDataManager;
    }

    private void modifyAttribute(Player player, int level, String configKey, Attribute attribute) {
        double attributeValue = getAttributeValue(configKey, level);
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(attributeInstance.getBaseValue() + attributeValue);
        } else {
            plugin.getLogger().log(Level.INFO, "Attribute " + attribute + " is not accessible for player " + player.getName());
        }
    }

    private double getAttributeValue(String configKey, int level) {
        return configManager.getConfig().getDouble(configKey, 0.0) * level;
    }

    public void modifyLifeForce(Player player, int level) {
        modifyAttribute(player, level, "skill_attributes.life_force", Attribute.GENERIC_MAX_HEALTH);
    }

    public void modifyStrength(Player player, int level) {
        modifyAttribute(player, level, "skill_attributes.strength", Attribute.GENERIC_ATTACK_DAMAGE);
    }

    public void modifyTenacity(Player player, int level) {
        modifyAttribute(player, level, "skill_attributes.tenacity.toughness", Attribute.GENERIC_ARMOR_TOUGHNESS);
        modifyAttribute(player, level, "skill_attributes.tenacity.knock_back_resistance", Attribute.GENERIC_KNOCKBACK_RESISTANCE);
    }

    public void modifyHaste(Player player, int level) {
        modifyAttribute(player, level, "skill_attributes.haste.attack_speed", Attribute.GENERIC_ATTACK_SPEED);
        modifyAttribute(player, level, "skill_attributes.haste.movement_speed", Attribute.GENERIC_MOVEMENT_SPEED);
    }

    public double modifyPrecision(int level) {
        return getAttributeValue("skill_attributes.precision.critical_chance", level) / 100;
    }

    public double modifyFerocity(int level) {
        return getAttributeValue("skill_attributes.ferocity.critical_damage", level) / 100;
    }


    public double getModifiedValue(String attributeName, int level) {
        switch (attributeName) {
            case "Life_Force":
                return getAttributeValue("skill_attributes.life_force", level);
            case "Strength":
                return getAttributeValue("skill_attributes.strength", level);
            case "Tenacity":
                return getAttributeValue("skill_attributes.tenacity.toughness", level)
                        + getAttributeValue("skill_attributes.tenacity.knock_back_resistance", level);
            case "Haste":
                return getAttributeValue("skill_attributes.haste.attack_speed", level)
                        + getAttributeValue("skill_attributes.haste.movement_speed", level);
            case "Precision":
                return getAttributeValue("skill_attributes.precision.critical_chance", level) / 100;
            case "Ferocity":
                return getAttributeValue("skill_attributes.ferocity.critical_damage", level) / 100;
            default:
                return 0.0;
        }
    }


    public void applyModifierToPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();

        // Reset all attributes to default for the specific player
        resetAllAttributesToDefault(player);

        // Apply Life Force modifier
        int lifeForceLevel = getAttributeLevel(playerUUID, "Life_Force");
        if (lifeForceLevel > 0) {
            modifyLifeForce(player, lifeForceLevel);
        }

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
            // Subtract one skill point and increase the attribute level
            updatePlayerSkillPoints(playerUUID, currentSkillPoints - 1);
            increaseAttributeLevel(playerUUID, attributeName);

            // Send a message to the player indicating the attribute level has increased
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                sendLevelUpMessage(player, attributeName, getAttributeLevel(playerUUID, attributeName));
                applyModifierToPlayer(player);
            }
        } else {
            // Send a message to the player indicating they don't have enough skill points
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                sendInsufficientSkillPointsMessage(player, attributeName);
            }
        }
    }

    private void updatePlayerSkillPoints(UUID playerUUID, int newSkillPoints) {
        playerDataManager.setPlayerSkillPoints(playerUUID, newSkillPoints);
    }

    private void increaseAttributeLevel(UUID playerUUID, String attributeName) {
        int currentAttributeLevel = getAttributeLevel(playerUUID, attributeName);
        setAttributeLevel(playerUUID, attributeName, currentAttributeLevel + 1);
    }

    private void sendLevelUpMessage(Player player, String attributeName, int newLevel) {
        player.sendMessage(Component.text("Leveled ", NamedTextColor.GREEN)
                .append(Component.text(attributeName, NamedTextColor.AQUA))
                .append(Component.text(" to ", NamedTextColor.GREEN))
                .append(Component.text(String.valueOf(newLevel), NamedTextColor.AQUA))
                .append(Component.text("!", NamedTextColor.GREEN)));
    }

    private void sendInsufficientSkillPointsMessage(Player player, String attributeName) {
        player.sendMessage(Component.text("Not enough skill points to level up ", NamedTextColor.RED)
                .append(Component.text(attributeName, NamedTextColor.AQUA))
                .append(Component.text(".", NamedTextColor.RED)));
    }

    // Method to get the level of a specific attribute
    public int getAttributeLevel(UUID playerUUID, String attributeName) {
        File playerDataFile = playerDataManager.getPlayerDataFile(playerUUID);
        YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        return playerDataConfig.getInt("Attributes." + attributeName, 0);
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

    public List<String> getSkillValueString(String attributeName, int level) {
        List<String> skillValues = new ArrayList<>();
        switch (attributeName) {
            case "Tenacity":
                double toughnessValue = getAttributeValue("skill_attributes.tenacity.toughness", level);
                double knockBackResistanceValue = getAttributeValue("skill_attributes.tenacity.knock_back_resistance", level);
                skillValues.add("Toughness Value: " + String.format("%.2f", toughnessValue));
                skillValues.add("Knockback Resistance Value: " + String.format("%.2f", knockBackResistanceValue));
                break;
            case "Haste":
                double attackSpeedValue = getAttributeValue("skill_attributes.haste.attack_speed", level);
                double movementSpeedValue = getAttributeValue("skill_attributes.haste.movement_speed", level);
                skillValues.add("Attack Speed Value: " + String.format("%.2f", attackSpeedValue));
                skillValues.add("Movement Speed Value: " + String.format("%.2f", movementSpeedValue));
                break;
            case "Precision":
                double precisionValue = getAttributeValue("skill_attributes.precision.critical_chance", level);
                skillValues.add("Critical Chance Value: " + String.format("%.2f", precisionValue) + "%");
                break;
            case "Ferocity":
                double ferocityValue = getAttributeValue("skill_attributes.ferocity.critical_damage", level);
                skillValues.add("Critical Damage Value: " + "+" + String.format("%.2f", ferocityValue) + "%");
                break;
            default:
                double modifiedValue = getModifiedValue(attributeName, level);
                skillValues.add("Skill Value: " + String.format("%.2f", modifiedValue));
                break;
        }
        return skillValues;
    }

    public String getAttributeDescription(String attributeName) {
        switch (attributeName) {
            case "Life_Force":
                return "Increases max health by " + getAttributeValue("skill_attributes.life_force", 1) + " per level.";
            case "Strength":
                return "Increases attack damage by " + getAttributeValue("skill_attributes.strength", 1) + " per level.";
            case "Tenacity":
                return "Increases armor toughness by " + getAttributeValue("skill_attributes.tenacity.toughness", 1) + " and knockback resistance by " + getAttributeValue("skill_attributes.tenacity.knock_back_resistance", 1) + " per level.";
            case "Haste":
                return "Increases attack speed by " + getAttributeValue("skill_attributes.haste.attack_speed", 1) + " and movement speed by " + getAttributeValue("skill_attributes.haste.movement_speed", 1) + " per level.";
            case "Precision":
                return "Increase critical chance by " + getAttributeValue("skill_attributes.precision.critical_chance", 1) + "% per level.";
            case "Ferocity":
                return "Increase critical damage by " + getAttributeValue("skill_attributes.ferocity.critical_damage", 1) + "% per level.";
            default:
                return "Description not found.";
        }
    }
}

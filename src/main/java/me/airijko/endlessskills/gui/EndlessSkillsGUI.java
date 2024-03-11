package me.airijko.endlessskills.gui;

import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.skills.SkillAttributes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EndlessSkillsGUI {
    private final PlayerDataManager playerDataManager;
    private final SkillAttributes skillAttributes;
    private Inventory gui;

    public EndlessSkillsGUI(PlayerDataManager playerDataManager, SkillAttributes skillAttributes) {
        this.playerDataManager = playerDataManager;
        this.skillAttributes = skillAttributes;
    }

    public void skillAttributesGUI(Player player) {
        UUID playerUUID = player.getUniqueId();
        // Retrieve the player's total skill points
        int totalSkillPoints = playerDataManager.getPlayerSkillPoints(player.getUniqueId());

        // Create a new inventory with 9 slots (1 row)
        gui = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("Endless Skills"));

        // Create a list of attribute names
        List<String> attributes = Arrays.asList("Life_Force", "Strength", "Tenacity", "Haste", "Precision", "Ferocity");

        // Create a list of wool colors
        List<Material> woolColors = Arrays.asList(Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.LIGHT_BLUE_WOOL, Material.BLUE_WOOL);

        // Create a list of text colors
        List<NamedTextColor> textColors = Arrays.asList(NamedTextColor.RED, NamedTextColor.GOLD, NamedTextColor.YELLOW, NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.AQUA);

        // Iterate over the attributes list
        int slotIndex = 10;
        for (int i = 0; i < attributes.size(); i++) {
            if (slotIndex == 13) slotIndex++;  // Skip slot 13 for the Nether Star
            String attribute = attributes.get(i);
            int level = skillAttributes.getAttributeLevel(playerUUID, attribute);
            String description = skillAttributes.getAttributeDescription(attribute);
            gui.setItem(slotIndex, createWoolItem(woolColors.get(i), textColors.get(i), attribute, String.valueOf(level), description, attribute));
            slotIndex++;
        }

        ItemStack netherStar = createNetherStarItem(playerUUID, playerDataManager, totalSkillPoints);

        gui.setItem(13, netherStar);

        fillEmptySlots(gui);

        // Open the GUI for the player
        player.openInventory(gui);
    }

    private ItemStack createNetherStarItem(UUID playerUUID, PlayerDataManager playerDataManager, int totalSkillPoints) {
        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta netherStarMeta = netherStar.getItemMeta();
        if (netherStarMeta != null) {
            Player player = Bukkit.getPlayer(playerUUID);
            String playerName = player != null ? player.getName() : "Unknown Player";

            netherStarMeta.displayName(Component.text((playerName + " Stats").toUpperCase(), NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true));

            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Level " + playerDataManager.getPlayerLevel(playerUUID), NamedTextColor.AQUA));

            String[] attributes = {"Life_Force", "Strength", "Tenacity", "Haste", "Precision", "Ferocity"};
            for (String attribute : attributes) {
                int level = skillAttributes.getAttributeLevel(playerUUID, attribute);
                lore.add(Component.text(attribute + ": " + level, NamedTextColor.GRAY));
            }

            lore.add(Component.text("Available Skill Points: " + totalSkillPoints, NamedTextColor.YELLOW));
            netherStarMeta.lore(lore);

            netherStar.setItemMeta(netherStarMeta);
        }
        return netherStar;
    }

    private ItemStack createWoolItem(Material woolType, NamedTextColor color, String displayName, String attributeLevel, String description, String attributeName) {
        ItemStack woolItem = new ItemStack(woolType);
        ItemMeta meta = woolItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName, color));
            int level = Integer.parseInt(attributeLevel);
            List<String> additionalLines = skillAttributes.getSkillValueString(attributeName, level);
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Level: " + attributeLevel, NamedTextColor.LIGHT_PURPLE));
            lore.add(Component.text(description, NamedTextColor.GRAY));
            for (String line : additionalLines) {
                lore.add(Component.text(line, NamedTextColor.DARK_GRAY));
            }
            meta.lore(lore);
            woolItem.setItemMeta(meta);
        }
        return woolItem;
    }

    public Inventory getInventory() {
        return gui;
    }

    private void fillEmptySlots(Inventory inventory) {
        ItemStack invisibleItem = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = invisibleItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(" "));
            invisibleItem.setItemMeta(meta);
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, invisibleItem);
            }
        }
    }
}

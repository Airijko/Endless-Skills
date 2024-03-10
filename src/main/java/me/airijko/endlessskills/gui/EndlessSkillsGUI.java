package me.airijko.endlessskills.gui;

import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.skills.SkillAttributes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
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

        // Retrieve the attribute levels and add wool items for different skill points
        int lifeForceLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Life_Force");
        String lifeForceDescription = skillAttributes.getAttributeDescription("Life_Force");
        gui.setItem(10, createWoolItem(Material.RED_WOOL, NamedTextColor.RED, "Life Force", String.valueOf(lifeForceLevel), lifeForceDescription));

        int strengthLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Strength");
        String strengthDescription = skillAttributes.getAttributeDescription("Strength");
        gui.setItem(11, createWoolItem(Material.ORANGE_WOOL, NamedTextColor.GOLD, "Strength", String.valueOf(strengthLevel), strengthDescription));

        int tenacityLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Tenacity");
        String tenacityDescription = skillAttributes.getAttributeDescription("Tenacity");
        gui.setItem(12, createWoolItem(Material.YELLOW_WOOL, NamedTextColor.YELLOW, "Tenacity", String.valueOf(tenacityLevel), tenacityDescription));

        int hasteLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Haste");
        String hasteDescription = skillAttributes.getAttributeDescription("Haste");
        gui.setItem(14, createWoolItem(Material.LIME_WOOL, NamedTextColor.GREEN, "Haste", String.valueOf(hasteLevel), hasteDescription));

        int precisionLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Precision");
        String precisionDescription = skillAttributes.getAttributeDescription("Precision");
        gui.setItem(15, createWoolItem(Material.LIGHT_BLUE_WOOL, NamedTextColor.AQUA, "Precision", String.valueOf(precisionLevel), precisionDescription));

        int ferocityLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Ferocity");
        String ferocityDescription = skillAttributes.getAttributeDescription("Ferocity");
        gui.setItem(16, createWoolItem(Material.BLUE_WOOL, NamedTextColor.AQUA, "Ferocity", String.valueOf(ferocityLevel), ferocityDescription));

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

            netherStarMeta.lore(Arrays.asList(
                    Component.text("Level " + playerDataManager.getPlayerLevel(playerUUID), NamedTextColor.AQUA),
                    Component.text( "Available Skill Points: " + totalSkillPoints, NamedTextColor.GRAY)
            ));

            netherStar.setItemMeta(netherStarMeta);
        }
        return netherStar;
    }

    private ItemStack createWoolItem(Material woolType, NamedTextColor color, String displayName, String attributeLevel, String description) {
        ItemStack woolItem = new ItemStack(woolType);
        ItemMeta meta = woolItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName, color));
            meta.lore(Arrays.asList(Component.text("Level: " + attributeLevel, NamedTextColor.GRAY), Component.text(description, NamedTextColor.GRAY)));
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

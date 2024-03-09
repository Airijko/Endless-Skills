package me.airijko.endlessskills.gui;

import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.skills.SkillAttributes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EndlessSkillsGUI {
    private final PlayerDataManager playerDataManager;
    private final SkillAttributes skillAttributes;
    private Inventory gui;

    public EndlessSkillsGUI(PlayerDataManager playerDataManager, SkillAttributes skillAttributes) {
        this.playerDataManager = playerDataManager;
        this.skillAttributes = skillAttributes;
    }

    public void skillAttributesGUI(Player player, boolean refresh) {
        // Retrieve the player's total skill points
        int totalSkillPoints = playerDataManager.getPlayerSkillPoints(player.getUniqueId());

        // Create a new inventory with 9 slots (1 row)
        gui = Bukkit.createInventory(null, 9, "Endless Skills");

        // Retrieve the attribute levels and add wool items for different skill points
        int lifeForceLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Life_Force");
        String lifeForceDescription = skillAttributes.getAttributeDescription("Life_Force");
        addWoolItem(gui, Material.RED_WOOL, ChatColor.RED + "Life Force", String.valueOf(lifeForceLevel), lifeForceDescription);

        int strengthLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Strength");
        String strengthDescription = skillAttributes.getAttributeDescription("Strength");
        addWoolItem(gui, Material.ORANGE_WOOL, ChatColor.GOLD + "Strength", String.valueOf(strengthLevel), strengthDescription);

        int tenacityLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Tenacity");
        String tenacityDescription = skillAttributes.getAttributeDescription("Tenacity");
        addWoolItem(gui, Material.YELLOW_WOOL, ChatColor.YELLOW + "Tenacity", String.valueOf(tenacityLevel), tenacityDescription);

        int hasteLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Haste");
        String hasteDescription = skillAttributes.getAttributeDescription("Haste");
        addWoolItem(gui, Material.LIME_WOOL, ChatColor.GREEN + "Haste", String.valueOf(hasteLevel), hasteDescription);

        int precisionLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Precision");
        String precisionDescription = skillAttributes.getAttributeDescription("Precision");
        addWoolItem(gui, Material.LIGHT_BLUE_WOOL, ChatColor.AQUA + "Precision", String.valueOf(precisionLevel), precisionDescription);

        int ferocityLevel = skillAttributes.getAttributeLevel(player.getUniqueId(), "Ferocity");
        String ferocityDescription = skillAttributes.getAttributeDescription("Ferocity");
        addWoolItem(gui, Material.BLUE_WOOL, ChatColor.AQUA + "Ferocity", String.valueOf(ferocityLevel), ferocityDescription);

        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta netherStarMeta = netherStar.getItemMeta();
        if (netherStarMeta != null) {
            netherStarMeta.setDisplayName(ChatColor.GOLD + "Total Skill Points: " + totalSkillPoints);
            netherStar.setItemMeta(netherStarMeta);
        }

        gui.setItem(8, netherStar);

        fillEmptySlots(gui);

        // Open the GUI for the player
        player.openInventory(gui);

    }

    public Inventory getInventory() {
        return gui;
    }

    private void addWoolItem(Inventory gui, Material woolType, String displayName, String attributeLevel, String description) {
        ItemStack woolItem = new ItemStack(woolType);
        ItemMeta meta = woolItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Level: " + attributeLevel, ChatColor.GRAY + description));
            woolItem.setItemMeta(meta);
        }
        gui.addItem(woolItem);
    }

    private void fillEmptySlots(Inventory inventory) {
        ItemStack invisibleItem = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = invisibleItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            invisibleItem.setItemMeta(meta);
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, invisibleItem);
            }
        }
    }
}

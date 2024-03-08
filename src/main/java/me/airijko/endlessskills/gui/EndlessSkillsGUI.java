package me.airijko.endlessskills.gui;

import me.airijko.endlessskills.managers.PlayerDataManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;


public class EndlessSkillsGUI {
    private final PlayerDataManager playerDataManager;
    private Inventory gui;

    public EndlessSkillsGUI(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public Inventory openGUI(Player player) {
        // Retrieve the player's total skill points
        int totalSkillPoints = playerDataManager.getPlayerSkillPoints(player.getUniqueId());

        // Create a new inventory with 9 slots (1 row)
        gui = Bukkit.createInventory(null, 9, "Endless Skills");

        // Create wool items for different skill points
        addWoolItem(gui, Material.RED_WOOL, ChatColor.RED + "Life Force", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.ORANGE_WOOL, ChatColor.GOLD + "Strength", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.YELLOW_WOOL, ChatColor.YELLOW + "Tenacity", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.LIME_WOOL, ChatColor.GREEN + "Haste", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.LIGHT_BLUE_WOOL, ChatColor.AQUA + "Focus", ChatColor.GRAY + "NULL");

        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta netherStarMeta = netherStar.getItemMeta();
        if (netherStarMeta != null) {
            netherStarMeta.setDisplayName(ChatColor.GOLD + "Total Skill Points: " + totalSkillPoints);
            netherStar.setItemMeta(netherStarMeta);
        }

        gui.setItem(8, netherStar);

        // Open the GUI for the player
        player.openInventory(gui);

        return gui; // Return the Inventory instance
    }

    public Inventory getInventory() {
        return gui;
    }

    private void addWoolItem(Inventory gui, Material woolType, String displayName, String description) {
        ItemStack woolItem = new ItemStack(woolType);
        ItemMeta meta = woolItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Collections.singletonList(description)); // Set lore for the description
            woolItem.setItemMeta(meta);
        }
        gui.addItem(woolItem);
    }
}

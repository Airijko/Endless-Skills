package me.airijko.endlessskills.gui;

import me.airijko.endlessskills.managers.PlayerDataManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;


public class EndlessSkillsGUI implements Listener {
    private final PlayerDataManager playerDataManager;
    private final JavaPlugin plugin;

    public EndlessSkillsGUI(PlayerDataManager playerDataManager, JavaPlugin plugin) {
        this.playerDataManager = playerDataManager;
        this.plugin = plugin;

        // Register the listener in the constructor
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public Inventory openGUI(Player player) {
        // Retrieve the player's total skill points
        int totalSkillPoints = playerDataManager.getPlayerSkillPoints(player.getUniqueId());

        // Create a new inventory with 9 slots (1 row)
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.RED.toString() + ChatColor.BOLD + "Endless Skills" + ChatColor.BLACK + " - " + totalSkillPoints + " SP");

        // Create wool items for different skill points
        addWoolItem(gui, Material.RED_WOOL, ChatColor.RED + "Life Force", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.ORANGE_WOOL, ChatColor.GOLD + "Strength", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.YELLOW_WOOL, ChatColor.YELLOW + "Tenacity", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.LIME_WOOL, ChatColor.GREEN + "Haste", ChatColor.GRAY + "NULL");
        addWoolItem(gui, Material.LIGHT_BLUE_WOOL, ChatColor.AQUA + "Focus", ChatColor.GRAY + "NULL");

        // Open the GUI for the player
        player.openInventory(gui);

        return gui; // Return the Inventory instance
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

    // Add the InventoryClickEvent handler
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the clicked inventory is the one you created
        if (event.getInventory().equals(event.getWhoClicked().getOpenInventory().getTopInventory())) {
            // Cancel the event to prevent item interaction
            event.setCancelled(true);
        }
    }
}

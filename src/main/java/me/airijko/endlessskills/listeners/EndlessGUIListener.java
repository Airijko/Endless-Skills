package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.gui.EndlessSkillsGUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class EndlessGUIListener implements Listener {
    private final EndlessSkillsGUI endlessSkillsGUI;

    public EndlessGUIListener(EndlessSkillsGUI endlessSkillsGUI) {
        this.endlessSkillsGUI = endlessSkillsGUI;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Retrieve the Inventory object from the EndlessSkillsGUI instance
        Inventory guiInventory = endlessSkillsGUI.getInventory();

        // Check if the inventory involved in the event is the GUI inventory
        if (!event.getInventory().equals(guiInventory)) {
            return;
        }

        // Check if the clicked slot is within the GUI's bounds
        int guiSize = guiInventory.getSize();
        if (event.getRawSlot() >= 0 && event.getRawSlot() < guiSize) {
            event.setCancelled(true);
        }
    }
}

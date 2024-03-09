package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.gui.EndlessSkillsGUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(guiInventory)) {
            // Cancel the event to prevent any interaction with the custom GUI
            event.setCancelled(true);

            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }
        }
    }
}

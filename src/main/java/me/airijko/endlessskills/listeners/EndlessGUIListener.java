package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.skills.SkillAttributes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;
import java.util.logging.Logger;

public class EndlessGUIListener implements Listener {
    private final EndlessSkillsGUI endlessSkillsGUI;
    private final SkillAttributes skillAttributes;
    private final PlayerDataManager playerDataManager;
    private final Logger logger = Logger.getLogger(EndlessGUIListener.class.getName());

    public EndlessGUIListener(EndlessSkillsGUI endlessSkillsGUI, SkillAttributes skillAttributes, PlayerDataManager playerDataManager) {
        this.endlessSkillsGUI = endlessSkillsGUI;
        this.skillAttributes = skillAttributes;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Retrieve the Inventory object from the EndlessSkillsGUI instance
        Inventory guiInventory = endlessSkillsGUI.getInventory();

        // Log the event details for debugging purposes
        logger.info("InventoryClickEvent triggered. Clicked inventory: " + event.getClickedInventory());

        // Check if the inventory involved in the event is the GUI inventory
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(guiInventory)) {
            // Check for actions that could move items out of the GUI
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                    event.getAction() == InventoryAction.PICKUP_ALL ||
                    event.getAction() == InventoryAction.PICKUP_HALF ||
                    event.getAction() == InventoryAction.PICKUP_ONE ||
                    event.getAction() == InventoryAction.PICKUP_SOME) {
                // Cancel the event to prevent any interaction with the custom GUI
                event.setCancelled(true);

                // Log the cancellation of the event
                logger.info("Event cancelled. Action: " + event.getAction());
            }

            // Handle attribute level increase
            if (event.getCurrentItem() != null) {
                ItemMeta itemMeta = event.getCurrentItem().getItemMeta();

                if (itemMeta != null) {
                    String displayName = ChatColor.stripColor(itemMeta.getDisplayName());
                    Player player = (Player) event.getWhoClicked();
                    UUID playerUUID = player.getUniqueId();

                    switch (displayName) {
                        case "Life Force":
                            skillAttributes.useSkillPoint(playerUUID, "Life_Force");
                            break;
                        case "Strength":
                            skillAttributes.useSkillPoint(playerUUID, "Strength");
                            break;
                        case "Tenacity":
                            skillAttributes.useSkillPoint(playerUUID, "Tenacity");
                            break;
                        case "Haste":
                            skillAttributes.useSkillPoint(playerUUID, "Haste");
                            break;
                        case "Precision":
                            skillAttributes.useSkillPoint(playerUUID, "Precision");
                            break;
                        case "Ferocity":
                            skillAttributes.useSkillPoint(playerUUID, "Ferocity");
                            break;
                    }

                    endlessSkillsGUI.skillAttributesGUI(player, true);
                }
            }
        }
    }
}

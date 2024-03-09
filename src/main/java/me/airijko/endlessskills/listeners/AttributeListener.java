package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.managers.PlayerDataManager;
import me.airijko.endlessskills.skills.SkillAttributes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AttributeListener implements Listener {
    private final SkillAttributes skillAttributes;
    private final PlayerDataManager playerDataManager;
    private final EndlessSkillsGUI endlessSkillsGUI;

    public AttributeListener(SkillAttributes skillAttributes, PlayerDataManager playerDataManager, EndlessSkillsGUI endlessSkillsGUI) {
        this.skillAttributes = skillAttributes;
        this.playerDataManager = playerDataManager;
        this.endlessSkillsGUI = endlessSkillsGUI;
    }

    @EventHandler
    public void increaseAttributeLevel(InventoryClickEvent event) {
        // Retrieve the Inventory object from the EndlessSkillsGUI instance
        Inventory guiInventory = endlessSkillsGUI.getInventory();

        // Check if the inventory involved in the event is the GUI inventory
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(guiInventory)) {
            if (event.getCurrentItem() != null) {
                ItemMeta itemMeta = event.getCurrentItem().getItemMeta();

                if (itemMeta != null) {
                    String displayName = ChatColor.stripColor(itemMeta.getDisplayName());
                    Player player = (Player) event.getWhoClicked();
                    UUID playerUUID = player.getUniqueId();
                    System.out.println("Clicked player's ATTRIBUTE");

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
                     EndlessSkillsGUI endlessSkillsGUI = new EndlessSkillsGUI(playerDataManager, skillAttributes);
                     endlessSkillsGUI.skillAttributesGUI(player, true);
                }
            }
        }
    }
}

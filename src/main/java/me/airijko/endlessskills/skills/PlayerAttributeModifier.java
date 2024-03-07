package me.airijko.endlessskills.skills;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerAttributeModifier implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(event.getWhoClicked().getOpenInventory().getTopInventory())) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

                SkillAttributes skillAttributes = new SkillAttributes();
                switch (displayName) {
                    case "Life Force":
                        skillAttributes.modifyLifeForce((Player) event.getWhoClicked());
                        break;
                    case "Strength":
                        skillAttributes.modifyStrength((Player) event.getWhoClicked());
                        break;
                    case "Tenacity":
                        skillAttributes.modifyTenacity((Player) event.getWhoClicked());
                        break;
                    case "Haste":
                        skillAttributes.modifyHaste((Player) event.getWhoClicked());
                        break;
                    case "Focus":
                        skillAttributes.modifyFocus((Player) event.getWhoClicked());
                        break;
                }
            }
        }
    }
}

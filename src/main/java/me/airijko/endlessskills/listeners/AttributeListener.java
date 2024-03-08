package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.skills.SkillAttributes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AttributeListener implements Listener {
    private final SkillAttributes skillAttributes;

    public AttributeListener(SkillAttributes skillAttributes) {
        this.skillAttributes = skillAttributes;
    }

    @EventHandler
    public void increaseAttributeLevel(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            ItemMeta itemMeta = event.getCurrentItem().getItemMeta();

            if (itemMeta != null) {
                String displayName = ChatColor.stripColor(itemMeta.getDisplayName());
                Player player = (Player) event.getWhoClicked();
                UUID playerUUID = player.getUniqueId();

                switch (displayName) {
                    case "Life Force":
                        int Life_Force = skillAttributes.getAttributeLevel(playerUUID, "Life_Force");
                        skillAttributes.setAttributeLevel(playerUUID, "Life_Force", Life_Force + 1);
                        player.sendMessage("You've leveled up Life Force to level " + (Life_Force + 1));
                        break;
                    case "Strength":
                        int Strength = skillAttributes.getAttributeLevel(playerUUID, "Strength");
                        skillAttributes.setAttributeLevel(playerUUID, "Strength", Strength + 1);
                        player.sendMessage("You've leveled up Strength to level " + (Strength + 1));
                        break;
                    case "Tenacity":
                        int Tenacity = skillAttributes.getAttributeLevel(playerUUID, "Tenacity");
                        skillAttributes.setAttributeLevel(playerUUID, "Tenacity", Tenacity + 1);
                        player.sendMessage("You've leveled up Tenacity to level " + (Tenacity + 1));
                        break;
                    case "Haste":
                        int Haste = skillAttributes.getAttributeLevel(playerUUID, "Haste");
                        skillAttributes.setAttributeLevel(playerUUID, "Haste", Haste + 1);
                        player.sendMessage("You've leveled up Haste to level " + (Haste + 1));
                        break;
                    case "Focus":
                        int Focus = skillAttributes.getAttributeLevel(playerUUID, "Focus");
                        skillAttributes.setAttributeLevel(playerUUID, "Focus", Focus + 1);
                        player.sendMessage("You've leveled up Focus to level " + (Focus + 1));
                        break;
                }
            }
        }
    }
}

package me.airijko.endlessskills.listeners;

import me.airijko.endlessskills.skills.SkillAttributes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
                    case "Focus":
                        skillAttributes.useSkillPoint(playerUUID, "Focus");
                        break;
                }
            }
        }
    }
}

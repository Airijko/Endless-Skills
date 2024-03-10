package me.airijko.endlessskills.listeners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.airijko.endlessskills.gui.EndlessSkillsGUI;
import me.airijko.endlessskills.skills.SkillAttributes;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.UUID;

public class EndlessGUIListener implements Listener {
    private final EndlessSkillsGUI endlessSkillsGUI;
    private final SkillAttributes skillAttributes;

    public EndlessGUIListener(EndlessSkillsGUI endlessSkillsGUI, SkillAttributes skillAttributes) {
        this.endlessSkillsGUI = endlessSkillsGUI;
        this.skillAttributes = skillAttributes;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Retrieve the Inventory object from the EndlessSkillsGUI instance
        Inventory guiInventory = endlessSkillsGUI.getInventory();

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
            }

            // Handle attribute level increase
            if (event.getCurrentItem() != null) {
                ItemMeta itemMeta = event.getCurrentItem().getItemMeta();

                if (itemMeta != null) {
                    String displayName = GsonComponentSerializer.gson().serialize(Objects.requireNonNull(itemMeta.displayName()));
                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(displayName).getAsJsonObject();
                    String text = jsonObject.get("text").getAsString();

                    Player player = (Player) event.getWhoClicked();
                    UUID playerUUID = player.getUniqueId();

                    switch (text) {
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

                    endlessSkillsGUI.skillAttributesGUI(player);
                }
            }
        }
    }
}

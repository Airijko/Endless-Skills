package me.airijko.endlessskills.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

import java.util.HashMap;
import java.util.Map;
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
    public void handleEndlessGUI(InventoryClickEvent event) {
        // Retrieve the Inventory object from the EndlessSkillsGUI instance
        Inventory guiInventory = endlessSkillsGUI.getInventory();

        // Check if the inventory involved in the event is the GUI inventory
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(guiInventory)) {

            // Check for actions that could move items out of the GUI
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(guiInventory)) {
                cancelItemMovement(event);

                // Handle attribute level increase
                Player player = (Player) event.getWhoClicked();
                UUID playerUUID = player.getUniqueId();

                Map<String, Runnable> actionMap = new HashMap<>();
                actionMap.put("Life_Force", () -> skillAttributes.useSkillPoint(playerUUID, "Life_Force"));
                actionMap.put("Strength", () -> skillAttributes.useSkillPoint(playerUUID, "Strength"));
                actionMap.put("Tenacity", () -> skillAttributes.useSkillPoint(playerUUID, "Tenacity"));
                actionMap.put("Haste", () -> skillAttributes.useSkillPoint(playerUUID, "Haste"));
                actionMap.put("Precision", () -> skillAttributes.useSkillPoint(playerUUID, "Precision"));
                actionMap.put("Ferocity", () -> skillAttributes.useSkillPoint(playerUUID, "Ferocity"));

                handleAction(event, actionMap);

                endlessSkillsGUI.skillAttributesGUI(player);
            }
        }
    }

    private void cancelItemMovement(InventoryClickEvent event) {
        // Check for actions that could move items out of the GUI
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                event.getAction() == InventoryAction.PICKUP_ALL ||
                event.getAction() == InventoryAction.PICKUP_HALF ||
                event.getAction() == InventoryAction.PICKUP_ONE ||
                event.getAction() == InventoryAction.PICKUP_SOME) {
            // Cancel the event to prevent any interaction with the custom GUI
            event.setCancelled(true);
        }
    }

    private void handleAction(InventoryClickEvent event, Map<String, Runnable> actionMap) {
        if (event.getCurrentItem() != null) {
            ItemMeta itemMeta = event.getCurrentItem().getItemMeta();

            if (itemMeta != null) {
                String displayName = GsonComponentSerializer.gson().serialize(Objects.requireNonNull(itemMeta.displayName()));
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(displayName, JsonObject.class);
                String text = jsonObject.get("text").getAsString();

                Runnable action = actionMap.get(text);
                if (action != null) {
                    action.run();
                }
            }
        }
    }
}
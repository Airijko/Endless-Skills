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
import java.util.logging.Logger;

public class EndlessGUIListener implements Listener {
    private final EndlessSkillsGUI endlessSkillsGUI;
    private final SkillAttributes skillAttributes;
    private final Logger logger = Logger.getLogger(EndlessGUIListener.class.getName());

    public EndlessGUIListener(EndlessSkillsGUI endlessSkillsGUI, SkillAttributes skillAttributes) {
        this.endlessSkillsGUI = endlessSkillsGUI;
        this.skillAttributes = skillAttributes;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        logger.info("Inventory click event detected.");

        // Retrieve the Inventory object from the EndlessSkillsGUI instance
        Inventory guiInventory = endlessSkillsGUI.getInventory();

        // Check if the inventory involved in the event is the GUI inventory
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(guiInventory)) {
            logger.info("Clicked inventory is the GUI inventory.");

            // Check for actions that could move items out of the GUI
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                    event.getAction() == InventoryAction.PICKUP_ALL ||
                    event.getAction() == InventoryAction.PICKUP_HALF ||
                    event.getAction() == InventoryAction.PICKUP_ONE ||
                    event.getAction() == InventoryAction.PICKUP_SOME) {
                // Cancel the event to prevent any interaction with the custom GUI
                event.setCancelled(true);
                logger.info("Event cancelled due to item movement.");
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

                    logger.info("Display name: " + text);

                    switch (text) {
                        case "Life Force":
                            skillAttributes.useSkillPoint(playerUUID, "Life_Force");
                            logger.info("Skill point used for Life Force.");
                            break;
                        case "Strength":
                            skillAttributes.useSkillPoint(playerUUID, "Strength");
                            logger.info("Skill point used for Strength.");
                            break;
                        case "Tenacity":
                            skillAttributes.useSkillPoint(playerUUID, "Tenacity");
                            logger.info("Skill point used for Tenacity.");
                            break;
                        case "Haste":
                            skillAttributes.useSkillPoint(playerUUID, "Haste");
                            logger.info("Skill point used for Haste.");
                            break;
                        case "Precision":
                            skillAttributes.useSkillPoint(playerUUID, "Precision");
                            logger.info("Skill point used for Precision.");
                            break;
                        case "Ferocity":
                            skillAttributes.useSkillPoint(playerUUID, "Ferocity");
                            logger.info("Skill point used for Ferocity.");
                            break;
                    }

                    endlessSkillsGUI.skillAttributesGUI(player);
                    logger.info("Skill attributes GUI updated.");
                }
            }
        }
    }
}

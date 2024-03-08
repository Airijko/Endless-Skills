package me.airijko.endlessskills.commands;

import me.airijko.endlessskills.skills.SkillAttributes;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetAttributesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;

        // Reset attributes to their default values
        SkillAttributes.resetAllAttributesToDefault(player);

        player.sendMessage("Your attributes have been reset to their default values.");
        return true;
    }

    private void resetAttribute(Player player, Attribute attribute, double defaultValue) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(defaultValue);
        }
    }
}

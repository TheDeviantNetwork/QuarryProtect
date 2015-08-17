package com.thedeviantnetwork.quarryprotect.commands;

import com.thedeviantnetwork.quarryprotect.QuarryProtectPlugin;
import com.thedeviantnetwork.quarryprotect.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleQuarry implements CommandExecutor {

    private QuarryProtectPlugin plugin;

    public ToggleQuarry(QuarryProtectPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && commandSender.hasPermission("quarryprotect.toggle") &&
                ((Player) commandSender).getItemInHand().getType() != Material.AIR) {
            if (plugin.toggleQuarry(((Player) commandSender).getItemInHand()))
                Util.sendMessage(commandSender, "Enabled!");
            else
                Util.sendMessage(commandSender, ChatColor.RED + "Disabled!");
            return true;
        }
        return false;
    }
}

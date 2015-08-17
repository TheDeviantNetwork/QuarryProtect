package com.thedeviantnetwork.quarryprotect;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {

    public static void sendMessage(CommandSender commandSender, String message){
        commandSender.sendMessage(parseMessage(message));
    }

    public static String parseMessage(String message){
        return ChatColor.GREEN + "[QuarryProtect]" + ChatColor.AQUA + " " + message;
    }
}

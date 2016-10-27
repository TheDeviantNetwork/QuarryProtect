package com.thedeviantnetwork.quarryprotect.commands;

import com.google.common.base.Optional;
import com.thedeviantnetwork.quarryprotect.Util;
import com.thedeviantnetwork.quarryprotect.region.QuarryRegion;
import com.thedeviantnetwork.quarryprotect.region.QuarryRegionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrustCommand implements CommandExecutor {

    private QuarryRegionManager regionManager;

    public TrustCommand(QuarryRegionManager regionManager){
        this.regionManager = regionManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1){
            Player sender = (Player) commandSender;
            Optional<OfflinePlayer> target = Optional.fromNullable(Bukkit.getOfflinePlayer(strings[0]));
            Optional<QuarryRegion> region = regionManager.getRegion(sender.getLocation(), false);

            if(!target.isPresent()){
                Util.sendMessage(commandSender, "Player " + strings[0] + " can't be found");
            }else if (!region.isPresent()){
                Util.sendMessage(commandSender, "You are not standing in the protected area!");
            }else {
                region.get().addPlayer(target.get());
                Util.sendMessage(commandSender, "Player " + strings[0] + " added!" );
            }
            return true;
        }
        Util.sendMessage(commandSender, ChatColor.YELLOW + "/trust <player>");
        return false;
    }
}

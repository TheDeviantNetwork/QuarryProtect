package com.thedeviantnetwork.quarryprotect.commands;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.thedeviantnetwork.quarryprotect.Util;
import com.thedeviantnetwork.quarryprotect.region.QuarryRegion;
import com.thedeviantnetwork.quarryprotect.region.QuarryRegionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class TpQuarryCommand implements CommandExecutor {

    public QuarryRegionManager quarryRegionManager;

    public TpQuarryCommand(QuarryRegionManager quarryRegionManager){
        this.quarryRegionManager = quarryRegionManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1 && commandSender instanceof Player)
            try {
                int i = Integer.parseInt(strings[0]);
                Player player = (Player) commandSender;
                quarryRegionManager.checkRegionsFor(player);
                List<QuarryRegion> quarryRegionList = quarryRegionManager.getRegionList(player);
                if (quarryRegionList.size() > i){
                    QuarryRegion region = quarryRegionList.get(i);
                    player.teleport(BukkitUtil.toLocation(region.getWorld(), region.getBlock().add(0,4,0)), PlayerTeleportEvent.TeleportCause.COMMAND);
                    Util.sendMessage(commandSender, "Teleporting to Quarry " + i);
                }else if (quarryRegionList.size() == 0)
                    Util.sendMessage(commandSender, "You have no quarries");
                else
                    Util.sendMessage(commandSender, "No quarry with id: " + i);
                return true;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        Util.sendMessage(commandSender, ChatColor.YELLOW + "/tpquarry <quarrynumber>");
        return false;
    }
}

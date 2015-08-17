package com.thedeviantnetwork.quarryprotect.listener;

import com.google.common.base.Optional;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.thedeviantnetwork.quarryprotect.QuarryProtectPlugin;
import com.thedeviantnetwork.quarryprotect.Util;
import com.thedeviantnetwork.quarryprotect.region.QuarryRegion;
import com.thedeviantnetwork.quarryprotect.region.QuarryRegionManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerListener implements Listener {

    private QuarryProtectPlugin plugin;

    public PlayerListener(QuarryProtectPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event){
        if (!plugin.isQuarry(event.getBlock()))
            return;

        QuarryRegionManager quarryRegionManager = plugin.getQuarryRegionManager();
        quarryRegionManager.checkRegionsFor(event.getPlayer());

        int limit = plugin.getLimit(event.getPlayer());
        if (limit <= quarryRegionManager.getRegionList(event.getPlayer()).size()) {
            Util.sendMessage(event.getPlayer(), ChatColor.RED + "You can't place any more quarries!!!!");
            Util.sendMessage(event.getPlayer(), ChatColor.RED + "Consider removing a old quarry and try again!!!");
            event.setCancelled(true);
            return;
        }

        quarryRegionManager.createRegion(event.getPlayer(), event.getBlock().getLocation());
        Util.sendMessage(event.getPlayer(), "Made a protected area around your quarry!");
        Util.sendMessage(event.getPlayer(), "do /trust <friend> to allow players on this area!");
        Util.sendMessage(event.getPlayer(), "do /untrust <friend> to remove players on this area!");
        Util.sendMessage(event.getPlayer(), "do /tpquarry <nr> (starting with 0) to teleport to your quarry");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        World world = event.getPlayer().getWorld();
        QuarryRegionManager quarryRegionManager = plugin.getQuarryRegionManager();
        Optional<QuarryRegion> region = quarryRegionManager.getRegion(event.getBlock().getLocation(), true);
        if (region.isPresent()){
            Location location = BukkitUtil.toLocation(world, region.get().getBlock());
            quarryRegionManager.removeRegion(location);
            Util.sendMessage(event.getPlayer(), "Removed protection!!");
        }
    }

}

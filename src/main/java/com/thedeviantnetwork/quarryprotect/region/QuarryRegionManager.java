package com.thedeviantnetwork.quarryprotect.region;

import com.google.common.base.Optional;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuarryRegionManager {

    @Getter
    private final List<QuarryRegion> quarryRegions = new ArrayList<QuarryRegion>();

    private WorldGuardPlugin worldGuard;

    public QuarryRegionManager(WorldGuardPlugin worldGuard){
        this.worldGuard = worldGuard;

        for (World world : Bukkit.getWorlds()) {
           for(ProtectedRegion region : worldGuard.getRegionManager(world).getRegions().values())
                if (region instanceof ProtectedCuboidRegion && QuarryRegion.isQuarryRegion(region))
                    quarryRegions.add(new QuarryRegion((ProtectedCuboidRegion) region, world));
        }
    }

    public void createRegion(Player player, Location loc){
        QuarryRegion region = new QuarryRegion(player, 15, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
        regionManager.addRegion(region.getRegion());
        try {
            regionManager.saveChanges();
        } catch (StorageException e) {
            e.printStackTrace();
        }
        quarryRegions.add(region);
    }

    public Optional<QuarryRegion> getRegion(Location location, boolean exact){
        for (QuarryRegion region : quarryRegions){
            if(exact ? region.getBlock().equals(BukkitUtil.toVector(location)) : region.contains(BukkitUtil.toVector(location)) )
                return Optional.of(region);
        }
        return Optional.absent();
    }

    public List<QuarryRegion> getRegionList(Player player){
        List<QuarryRegion> regions = new ArrayList<QuarryRegion>();
        for (QuarryRegion quarryRegion : quarryRegions)
            if (quarryRegion.getPlayer().equals(player.getUniqueId()))
                regions.add(quarryRegion);
        return regions;
    }

    public void removeRegion(Location loc){
        Iterator<QuarryRegion> iterator = quarryRegions.iterator();
        while (iterator.hasNext()) {
            QuarryRegion region = iterator.next();
            if (region.getBlock().equals(BukkitUtil.toVector(loc))) {
                RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());
                regionManager.removeRegion(region.getRegion().getId());
                try {
                    regionManager.saveChanges();
                } catch (StorageException e) {
                    e.printStackTrace();
                }
                iterator.remove();
                return;
            }
        }
    }

    public void checkRegionsFor(Player player) {
        for (QuarryRegion region : getRegionList(player)) {
            Location location = BukkitUtil.toLocation(player.getWorld(), region.getBlock());
            if (location.getBlock().getType() == Material.AIR) {
                removeRegion(location);
            }
        }
    }
}

package com.thedeviantnetwork.quarryprotect.region;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.thedeviantnetwork.quarryprotect.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class QuarryRegion {

    @Getter
    private final ProtectedCuboidRegion region;
    @Getter
    private final UUID player;
    @Getter
    private final BlockVector block;

    private final World world;

    public QuarryRegion(Player player, int size, int x, int y, int z) {
        this.world = player.getWorld();
        this.block = new BlockVector(x,y,z);
        this.player = player.getUniqueId();
        BlockVector v1 = block.add(size, 0, size).setY(265).toBlockVector();
        BlockVector v2 = block.add(-size, 0, -size).setY(0).toBlockVector();
        this.region = new ProtectedCuboidRegion(generateRegionName(player.getUniqueId(), block), v1, v2);
        this.region.setFlag(DefaultFlag.GREET_MESSAGE, Util.parseMessage("Entering " + player.getName() + "'s protected area"));
        this.region.setFlag(DefaultFlag.FAREWELL_MESSAGE, Util.parseMessage("Leaving " + player.getName() + "'s protected area"));
        this.region.getOwners().addPlayer(player.getUniqueId());
    }

    public QuarryRegion(ProtectedCuboidRegion region, World world){
        this.region = region;
        String[] data = region.getId().split("_");
        this.player = region.getOwners().getUniqueIds().iterator().next();
        this.block = new BlockVector(Integer.valueOf(data[1]), Integer.valueOf(data[2]), Integer.valueOf(data[3]));
        this.world = world;
    }

    private static String generateRegionName(UUID player, Vector loc){
        return "qp_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }

    public static boolean isQuarryRegion(ProtectedRegion region){
        if (region.getId().startsWith("qp") && region.getId().contains("_")) {
            String[] data = region.getId().split("_");
            return data.length == 4 && data[0].equals("qp");
        }
        else return false;
    }

    boolean contains(Vector vector){
        return region.contains(vector);
    }


    public World getWorld() {
        return world;
    }

    public void addPlayer(OfflinePlayer player){
        region.getMembers().addPlayer(player.getUniqueId());
    }

    public void delPlayer(OfflinePlayer offlinePlayer) {
        region.getMembers().removePlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public String toString() {
        return "QuarryRegion{" +
                "region=" + region +
                ", player=" + player +
                ", block=" + block +
                '}';
    }


}

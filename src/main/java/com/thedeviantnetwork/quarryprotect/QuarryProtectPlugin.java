package com.thedeviantnetwork.quarryprotect;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.thedeviantnetwork.quarryprotect.commands.ToggleQuarry;
import com.thedeviantnetwork.quarryprotect.commands.TpQuarryCommand;
import com.thedeviantnetwork.quarryprotect.commands.TrustCommand;
import com.thedeviantnetwork.quarryprotect.commands.UnTrustCommand;
import com.thedeviantnetwork.quarryprotect.listener.PlayerListener;
import com.thedeviantnetwork.quarryprotect.region.QuarryRegionManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuarryProtectPlugin extends JavaPlugin {

    @Getter
    private static Logger log;
    @Getter
    private WorldGuardPlugin worldGuard;

    private QuarryRegionManager regionManager;

    private Map<String, Integer> limits = new HashMap<String, Integer>();

    private List<String> blocks;

    @Override
    public void onEnable() {
        log = getLogger();
        getLog().info("Lets protect some quarries!!!");
        Object wg =  getServer().getPluginManager().getPlugin("WorldGuard");
        if(wg != null && wg instanceof WorldGuardPlugin)
            worldGuard = (WorldGuardPlugin) wg;
        else {
            getLog().info("WorldGuard isn't installed can't continue");
            setEnabled(false);
        }

        readConfig();
        this.regionManager = new QuarryRegionManager(worldGuard, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        registerCommands();
    }

    @Override
    public void onDisable() {
        getLog().info("I am losing grip on the quarries, bye!!!");
        super.onDisable();
    }

    public QuarryRegionManager getQuarryRegionManager(){
        return regionManager;
    }

    private void readConfig(){
        saveDefaultConfig();
        this.blocks = getConfig().getStringList("blocks");
        for (String perm : getConfig().getConfigurationSection("limits").getKeys(false)){
            limits.put("quarryprotect.limit." + perm, getConfig().getInt("limits." + perm));
        }
    }

    public boolean toggleQuarry(ItemStack itemStack){
        boolean returnvalue = false;
        List<String> blocks = getConfig().getStringList("blocks");
        String name = itemStack.getType().name() + "!" + itemStack.getData().getData();
        if (blocks.contains(name))
            blocks.remove(name);
        else {
            blocks.add(name);
            returnvalue = true;
        }
        getConfig().set("blocks", blocks);
        saveConfig();
        readConfig();
        return returnvalue;
    }

    public boolean isQuarry(Block block){
        return blocks.contains(block.getType().name() + "!" + block.getData());
    }

    public boolean isQuarry(ItemStack block){
        return blocks.contains(block.getType().name() + "!" + block.getData().getData());
    }

    public int getLimit(Player player){
        int limit = 1;
        for (String perm : limits.keySet()){
            if (player.hasPermission(perm)) {
                int i = limits.get(perm);
                if (limit < i)
                    limit = i;
            }
        }
        return limit;
    }

    private void registerCommands(){
        getCommand("togglequarry").setExecutor(new ToggleQuarry(this));
        getCommand("tpquarry").setExecutor(new TpQuarryCommand(regionManager));
        getCommand("trust").setExecutor(new TrustCommand(regionManager));
        getCommand("untrust").setExecutor(new UnTrustCommand(regionManager));
    }
}

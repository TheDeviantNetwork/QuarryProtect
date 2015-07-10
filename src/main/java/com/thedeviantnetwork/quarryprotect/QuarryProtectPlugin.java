package com.thedeviantnetwork.quarryprotect;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class QuarryProtectPlugin extends JavaPlugin {

    @Getter
    private static Logger log;

    @Override
    public void onEnable() {
        log = getLogger();
        getLog().info("Lets protect some quarries");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        getLog().info("I am losing grip on the quarries, bye!!!");
        super.onDisable();
    }
}

package com.itsaloof.kitpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.itsaloof.kitpvp.KitPvPPlugin;

public class SignEvent implements Listener {

    private final KitPvPPlugin pl;

    public SignEvent(KitPvPPlugin plugin) {
    	this.pl = plugin;
    }


    @EventHandler
    public void onSign(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[competitive]")) {
            e.setLine(0, "§8[§6Competitive§8]");
            e.setLine(1, "Queued: " + pl.queue.size());
        } else {
            return;
        }
    }

}

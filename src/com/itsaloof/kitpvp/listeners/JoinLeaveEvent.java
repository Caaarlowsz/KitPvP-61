package com.itsaloof.kitpvp.listeners;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.utils.CPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvent implements Listener {
    KitPvPPlugin pl;

    public JoinLeaveEvent(KitPvPPlugin plugin) {
        pl = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CPlayer cp = new CPlayer(e.getPlayer(), pl);
        pl.players.put(e.getPlayer(), cp);
        cp.save();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        CPlayer cp = pl.players.get(e.getPlayer());
        cp.save();
    }

}

package com.itsaloof.kitpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.api.events.ArenaJoinEvent;
import com.itsaloof.kitpvp.api.events.PlayerWinEvent;
import com.itsaloof.kitpvp.utils.Arena;

public class ArenaListener implements Listener {
	
	@SuppressWarnings("unused")
	private final KitPvPPlugin plugin;
	
	public ArenaListener(final KitPvPPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onArenaJoin(ArenaJoinEvent event)
	{
		Arena a = event.getArena();
		if(a.isArenaFull())
			event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onPlayerWin(PlayerWinEvent event)
	{
		
	}

}

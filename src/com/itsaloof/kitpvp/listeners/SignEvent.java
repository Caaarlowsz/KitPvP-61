package com.itsaloof.kitpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.itsaloof.kitpvp.KitPvPPlugin;

public class SignEvent implements Listener{
	
	KitPvPPlugin pl;
	
	public SignEvent(KitPvPPlugin plugin)
	{
		
	}
	
	
	@EventHandler
	public void onSign(SignChangeEvent e)
	{
		if(e.getLine(0).equalsIgnoreCase("[competitive]"))
		{
			e.setLine(0, "§8[§6Competitive§8]");
			e.setLine(1, "Queued: 0");
		}else
		{
			return;
		}
	}

}

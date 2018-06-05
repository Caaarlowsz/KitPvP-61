package com.itsaloof.kitpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.itsaloof.kitpvp.api.events.RegisterEvent;

import net.md_5.bungee.api.ChatColor;

public class RegisterListener implements Listener{
	
	public RegisterListener()
	{
		
	}
	
	
	@EventHandler
	public void onRegister(RegisterEvent event)
	{
		if(event.isCancelled())
			return;

		event.getPlayer().sendMessage(ChatColor.GREEN + "You are now registered as " + event.getUser().getName());
		event.getUser().openPrivateChannel().complete().sendMessage(event.getUser().getAsMention() + " you are now registered on KnowledgeCraft as " + event.getPlayer().getName()).queue();
	}

}

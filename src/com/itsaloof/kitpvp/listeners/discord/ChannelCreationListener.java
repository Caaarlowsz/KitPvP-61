package com.itsaloof.kitpvp.listeners.discord;

import java.awt.Color;

import com.itsaloof.kitpvp.KitPvPPlugin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChannelCreationListener extends ListenerAdapter {

	private final KitPvPPlugin plugin;
	
	public ChannelCreationListener(KitPvPPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) 
	{
		if(isBotChannel(event.getChannel().getName()))
		{
			EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(plugin.api.asBot().getApplicationInfo().complete().getName(), null, plugin.api.asBot().getApplicationInfo().complete().getIconUrl());
			builder.setTitle("Bot Channel", null);
			builder.setDescription("This will be where you use bot-commands for AloofBot or any other bot");
			builder.setColor(new Color(0, 255, 55));
			event.getChannel().sendMessage(builder.build()).queue();
		}
	}
	
	private boolean isBotChannel(String channel)
	{
		String botChannel = plugin.getConfig().getString("bot-channel");
		if(botChannel.equals(null) || botChannel.equals(""))
			return false;
		return (botChannel.equals(channel));
	}
	
}

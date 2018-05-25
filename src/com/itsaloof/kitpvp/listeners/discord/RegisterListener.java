package com.itsaloof.kitpvp.listeners.discord;

import java.io.File;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.itsaloof.kitpvp.KitPvPPlugin;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class RegisterListener extends ListenerAdapter{
	KitPvPPlugin plugin;
	
	public RegisterListener(KitPvPPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		String msg = event.getMessage().getContentRaw();
		if(msg.charAt(0) == '?')
		{
			msg.replace('?', ' ').trim();
			if(msg.toLowerCase().equalsIgnoreCase("register"))
			{
				PrivateChannel pc = (PrivateChannel) event.getMember().getUser().openPrivateChannel();
				File f = isRegistered(event.getMember().getUser().getDiscriminator());
				if(f == null)
				{
					pc.sendMessage("You are currently registered as " + YamlConfiguration.loadConfiguration(f).getString("Player.name")).queue();
					return;
				}else
				{
					UUID id = UUID.randomUUID();
					pc.sendMessage("Your UUID is " + id.toString() + "\ngo to KnowledgeCraft and run the command /register [UUID] to link your discord account to your minecraft account on the server!").queue();
					plugin.registration.put(id, event.getMember().getUser());
					return;
				}
			}
		}
	}
	
	public File isRegistered(String id)
	{
		for(File f : plugin.getFolder().listFiles())
		{
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			if(fc.getBoolean("Player.discord.registered"))
			{
				if(fc.getString("Player.discord.ID") == id)
				{
					return f;
				}else
				{
					continue;
				}
			}else
			{
				continue;
			}
		}
		return null;
	}

}

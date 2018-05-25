package com.itsaloof.kitpvp.listeners.discord;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PingListener extends ListenerAdapter {
	KitPvPPlugin plugin;
	
	public PingListener(KitPvPPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getMember().getUser().isBot())
			return;
		String msg = event.getMessage().getContentRaw();
		if(msg.charAt(0) == '?')
		{
			msg.replace('?', ' ').trim();
			if(msg.toLowerCase().contains("list"))
			{
				if(plugin.getServer().getOnlinePlayers().size() == 0)
				{
					event.getChannel().sendMessage("There are currently no players online!").queue();
					return;
				}
				String s = "<= Players Online =>";
				for(Player p : plugin.getServer().getOnlinePlayers())
				{
					s += "\n" + p.getName();
				}
				event.getChannel().sendMessage(s).queue();
				return;
			}
			if(msg.toLowerCase().contains("register"))
			{
				PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
				File f = isRegistered(event.getMember().getUser().getDiscriminator());
				if(f != null)
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
			else
			{
				event.getChannel().sendMessage("This bot is currently in beta we are working hard over at Aloof Inc. to get you all the features ready for you! :)").queue(new Consumer<Message>()
			    {
			        @Override
			        public void accept(Message t)
			        {
			            System.out.printf("Sent Message %s\n", t);
			        }
			    });
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

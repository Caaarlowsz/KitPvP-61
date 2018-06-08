package com.itsaloof.kitpvp.commands.discord;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;

public class RegisterDiscordCommand extends Command{
	private final KitPvPPlugin plugin;
	public RegisterDiscordCommand(KitPvPPlugin plugin)
	{
		this.name = "register";
		this.help = "Connects your MC account with your Discord account";
		this.plugin = plugin;
	}
	
	@Override
	protected void execute(CommandEvent event)
	{
		PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
		File f = isRegistered(KitPvPPlugin.getUniqueTag(event.getAuthor()));
		HashMap<Guild, User> userData = new HashMap<Guild, User>();
		userData.put(event.getGuild(), event.getAuthor());
		if(plugin.registration.containsValue(userData))
		{
			pc.sendMessage("You already have a pending UUID to use to register with\nUUID: " + getID(event.getMember().getUser())).queue();
			return;
		}
		if(f != null)
		{
			pc.sendMessage("You are currently registered as " + YamlConfiguration.loadConfiguration(f).getString("Player.name")).queue();
			return;
		}else
		{
			UUID id = UUID.randomUUID();
			pc.sendMessage("Your UUID key is **" + id.toString() + "**\ngo to KnowledgeCraft and run the command /register [UUID] to link your discord account to your minecraft account on the server!").queue();
			HashMap<Guild, User> user = new HashMap<Guild, User>();
			user.put(event.getGuild(), event.getAuthor());
			plugin.registration.put(id, user);
		}
	}
	
	private UUID getID(User user)
	{
		for(UUID ids : plugin.registration.keySet())
		{
			if(plugin.registration.get(ids).entrySet().iterator().next().getValue() == user)
			{
				return ids;
			}
		}
		return null;
	}
	
	public File isRegistered(String id)
	{
		for(File f : plugin.getFolder().listFiles())
		{
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			if(fc.getBoolean("Player.discord.registered"))
			{
				if(fc.getString("Player.discord.ID").trim().equals(id.trim()))
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

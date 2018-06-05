package com.itsaloof.kitpvp.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.api.events.RegisterEvent;
import com.itsaloof.kitpvp.utils.CPlayer;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.ChatColor;

public class RegisterCommand implements CommandExecutor{
	private KitPvPPlugin plugin;
	
	public RegisterCommand(KitPvPPlugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			if(cmd.getName().equalsIgnoreCase("register") && args.length >= 1)
			{
				UUID key = UUID.fromString(args[0]);
				if(args[0].matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") && plugin.registration.containsKey(key))
				{
					CPlayer p = plugin.players.get((Player) sender);
					if(p.isRegistered())
					{
						sender.sendMessage(ChatColor.RED + "You are already registered as " + p.getDiscordID() + "\nif this is a mistake please contact a server admin");
						return false;
					}else
					{
						HashMap<Guild, User> userData = plugin.registration.get(key);
						User user = userData.entrySet().iterator().next().getValue();
						Guild guild = userData.entrySet().iterator().next().getKey();
						Role r = guild.getRolesByName("registered", true).get(0);
						
						p.register(KitPvPPlugin.getUniqueTag(user));
						guild.getController().addSingleRoleToMember(guild.getMember(user), r).queue();
						
						this.plugin.getServer().getPluginManager().callEvent(new RegisterEvent((Player) sender, user));
						
						plugin.registration.remove(key);
						return true;
					}
				}else
				{
					sender.sendMessage(ChatColor.RED + "You have entered an invalid UUID");
					return false;
				}
			}else
			{
				sender.sendMessage(ChatColor.RED + ">/register [uuid]");
				return false;
			}
		}
		return false;
	}
}

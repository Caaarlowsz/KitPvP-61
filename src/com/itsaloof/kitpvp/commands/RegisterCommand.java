package com.itsaloof.kitpvp.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.utils.CPlayer;

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
			if(cmd.toString().equalsIgnoreCase("register") && args.length >= 1)
			{
				UUID key = UUID.fromString(args[0]);
				if(plugin.registration.containsKey(key))
				{
					CPlayer p = plugin.players.get((Player) sender);
					if(p.isRegistered())
					{
						sender.sendMessage(ChatColor.RED + "You are already registered as " + p.getDiscordID() + "\nif this is a mistake please contact a server admin");
						return false;
					}else
					{
						p.register(plugin.registration.get(key).getDiscriminator());
						sender.sendMessage(ChatColor.GREEN + "You are now registered as " + p.getDiscordID());
						return true;
					}
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

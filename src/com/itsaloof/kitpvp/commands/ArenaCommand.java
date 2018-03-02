package com.itsaloof.kitpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.utils.ArenaBuilderUtil;

import net.md_5.bungee.api.ChatColor;

public class ArenaCommand implements CommandExecutor {
	private final KitPvPPlugin plugin;
	private final String noPerms;
	public ArenaCommand(final KitPvPPlugin plugin) {
		this.plugin = plugin;
		this.noPerms = ChatColor.RED + "You do not have permission to use this command!";
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase() == "arena") 
		{
			if (sender instanceof Player) 
			{
				Player player = (Player) sender;
				if (player.hasPermission("kitpvp.arena")) 
				{
					if (args.length > 0) 
					{
						if(createArg(args, player))
							return true;
						if(setSpawnArg(args, player))
							return true;
						if(setupArg(args, player))
							return true;
						
					} 
					else 
					{
						return false;
					}
				}else
				{
					player.sendMessage(noPerms);
					return false;
				}
			}
		}
		return false;
	}
	
	private boolean setupArg(String args[], Player player)
	{
		if(args[1].equalsIgnoreCase("setup"))
		{
			if(!player.hasPermission("kitpvp.arena.setup"))
			{
				player.sendMessage(noPerms);
				return false;
			}
			ArenaBuilderUtil ab = arenaBuilderExists(args[1]);
			if(ab != null)
			{
				ab.createArena(player);
				return true;
			}else
			{
				player.sendMessage(ChatColor.RED + "That arena doesn't exist!");
				return false;
			}
		}
		return false;
	}
	
	private boolean setSpawnArg(String args[], Player player)
	{
		if(args[1].equalsIgnoreCase("setspawn") || args[1].equalsIgnoreCase("sp"))
		{
			if(!player.hasPermission("kitpvp.arena.setspawn"))
			{
				player.sendMessage(noPerms);
				return false;
			}
			ArenaBuilderUtil ab = arenaBuilderExists(args[0]);
			if(ab != null)
			{
				ab.setSpawn(player.getLocation());
				return true;
			}
			else
			{
				player.sendMessage(ChatColor.RED + "That arena doesn't exist!");
				return false;
			}
		}
		return false;
	}
	
	private boolean createArg(String args[], Player player)
	{
		if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c"))
		{
			
			if(!player.hasPermission("kitpvp.arena.create"))
			{
				player.sendMessage(noPerms);
				return false;
			}
			
			if(args.length >= 3) // /arena create [name] [maxPlayers]
			{
				if(isInteger(args[2]))
				{
					new ArenaBuilderUtil(plugin, args[1], Integer.getInteger(args[2]));
					return true;
				}else
				{
					player.sendMessage(ChatColor.RED + ">/arena create [arenaName] [maxPlayers]");
					return false;
				}
			}else
			{
				player.sendMessage(ChatColor.RED + ">/arena create [arenaName] [maxPlayers]");
				return false;
			}
		}
		return false;
	}
	
	public ArenaBuilderUtil arenaBuilderExists(String name)
	{
		for(ArenaBuilderUtil arena : plugin.underConstruction)
		{
			if(arena.getArenaName().equalsIgnoreCase(name))
			{
				return arena;
			}
		}
		return null;
	}
	
	private boolean isInteger(String num)
	{
		if(num.isEmpty())
			return false;
		for(Character c : num.toCharArray())
		{
			if(Character.isDigit(c))
			{
				continue;
			}else
			{
				return false;
			}
		}
		return true;
	}

}

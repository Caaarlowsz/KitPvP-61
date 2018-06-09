package com.itsaloof.kitpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.utils.Arena;
import com.itsaloof.kitpvp.utils.ArenaBuilderUtil;

import net.md_5.bungee.api.ChatColor;

public class ArenaCommand implements CommandExecutor {
	private final KitPvPPlugin plugin;
	private final String noPerms;
	private final String createUsage = ChatColor.RED + ">/arena create [arenaName] [maxPlayers]";
	private final String setupUsage = ChatColor.RED + ">/arena [arenaName] setup";
	private final String setSpawnUsage = ChatColor.RED + ">/arena [arenaName] setspawn";

	public ArenaCommand(final KitPvPPlugin plugin) {
		this.plugin = plugin;
		this.noPerms = ChatColor.RED + "You do not have permission to use this command!";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("arena")) {
			Player player = (Player) sender;
			if (sender instanceof Player && (sender.hasPermission("kitpvp.arena") || sender.isOp())) {
					if (args.length != 0) {
						
						if (createArg(args, player) && (sender.hasPermission("kitpvp.arena.create") || sender.isOp()))
							return true;
						
						if (setSpawnArg(args, player) && (sender.hasPermission("kitpvp.arena.setspawn") || sender.isOp()))
							return true;
						
						if (setupArg(args, player) && (sender.hasPermission("kitpvp.arena.setup") || sender.isOp()))
							return true;
						
						if(listArg(args, player) && (sender.hasPermission("kitpvp.arena.list") || sender.isOp()))
							return true;
					} else {
						
						player.sendMessage(createUsage);
						player.sendMessage(setSpawnUsage);
						player.sendMessage(setupUsage);
						return false;
					}
			}else {
				player.sendMessage(noPerms);
				return false;
			}
		}
		return false;
	}

	private boolean setupArg(String args[], Player player) {
		if(args.length > 1)
		{
			if (args[1].equalsIgnoreCase("setup")) {
					
				ArenaBuilderUtil ab = arenaBuilderExists(args[0]);
				if (ab != null) {
					ab.createArena(player);
					player.sendMessage(ChatColor.GREEN + "Successfully created arena " + ab.getArenaName());
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "That arena doesn't exist!");
					return false;
				}
			}
		}
		return false;
	}

	private boolean setSpawnArg(String args[], Player player) {
		if (args.length > 1)
		{
			if (args[1].equalsIgnoreCase("setspawn") || args[1].equalsIgnoreCase("sp")) {
					
				ArenaBuilderUtil ab = arenaBuilderExists(args[0]);
				if (ab != null) {
					ab.setSpawn(player.getLocation());
					player.sendMessage(ChatColor.GREEN + "Set new spawn!");
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "That arena doesn't exist!");
					return false;
				}
			}
		}
		return false;
	}

	private boolean createArg(String args[], Player player) {
		if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
			if (args.length >= 3) // /arena create [name] [maxPlayers]
			{
				if (isInteger(args[2])) {
					plugin.underConstruction.add(new ArenaBuilderUtil(plugin, args[1], Integer.parseInt(args[2])));
					player.sendMessage(ChatColor.GREEN + "Successfully started creation of arena " + args[1]);
					return true;
				} else {
					player.sendMessage(createUsage);
					return false;
				}
			} else {
				player.sendMessage(createUsage);
				return false;
			}
		}
		return false;
	}
	
	private boolean listArg(String args[], Player player)
	{
		if(args.length >= 1)
		{
			if(args[0].equalsIgnoreCase("list"))
			{
				if(plugin.arenas.isEmpty())
				{
					player.sendMessage(ChatColor.RED + "There are no arenas created yet!");
					return true;
				}
				
				player.sendMessage(ChatColor.GREEN + "=====Arenas======");
				for(Arena a : plugin.arenas)
				{
					player.sendMessage((plugin.arenas.indexOf(a) + 1) + ".) " + a.getArenaName());
				}
				return true;
			}
		}
		return false;
	}

	public ArenaBuilderUtil arenaBuilderExists(String name) {
		for (final ArenaBuilderUtil arena : plugin.underConstruction) {
			if (arena.getArenaName().equalsIgnoreCase(name)) {
				return arena;
			}
		}
		return null;
	}

	private boolean isInteger(String num) {
		if (num.isEmpty())
			return false;
		for (Character c : num.toCharArray()) {
			if (Character.isDigit(c)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

}

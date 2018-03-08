package com.itsaloof.kitpvp.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;

import net.md_5.bungee.api.ChatColor;

public class ArenaBuilderUtil {
	
	private final int max;
	private final KitPvPPlugin plugin;
	private final String name;
	private List<Location> spawns;
	public ArenaBuilderUtil(KitPvPPlugin plugin, String name, int max)
	{
		this.plugin = plugin;
		this.name = name;
		this.max = max;
		spawns = new ArrayList<Location>();
	}
	
	public void setSpawn(final Location loc)
	{
		if(spawns.isEmpty())
		{
			spawns.add(loc);
			return;
		}
		else if(spawns.size() == max)
		{
			spawns.remove(0);
			spawns.add(loc);
		}else
		{
			spawns.add(loc);
		}
	}
	
	public String getArenaName()
	{
		return this.name;
	}
	
	public int getMaxPlayers()
	{
		return this.max;
	}
	
	public void createArena(final Player player)
	{
		if(spawns.size() != max)
		{
			player.sendMessage(ChatColor.RED + "You need to set " + (max - spawns.size()) + " more spawns!");
			return;
		}
		
		Arena arena = new Arena(plugin, spawns, name, max);
		plugin.arenas.add(arena);
	}

}

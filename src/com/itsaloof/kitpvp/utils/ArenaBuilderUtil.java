package com.itsaloof.kitpvp.utils;

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
		plugin.underConstruction.add(this);
	}
	
	public void setSpawn(Location loc)
	{
		if(spawns.size() < max)
		{
			spawns.remove(0);
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
	
	public void createArena(Player player)
	{
		if(spawns.size() != max)
		{
			player.sendMessage(ChatColor.RED + "You need to set " + (max - spawns.size()) + " more spawns!");
			return;
		}
		
		Arena arena = new Arena(plugin, spawns, name, max);
		arena.saveArena();
		plugin.arenas.add(arena);
		
	}

}

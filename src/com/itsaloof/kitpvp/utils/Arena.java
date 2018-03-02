package com.itsaloof.kitpvp.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.api.events.ArenaJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class Arena {
	private final KitPvPPlugin plugin;
	private List<Location> spawns;
	private List<Player> players;
	private String name;
	private final int maxPlayers;
	public Arena(KitPvPPlugin plugin, List<Location> spawns, String name, int max)
	{
		this.plugin = plugin;
		this.spawns = spawns;
		this.name = name;
		this.maxPlayers = max;
	}
	
	public List<Location> getSpawns()
	{
		return this.spawns;
	}
	
	public boolean isArenaFull()
	{
		if(players.size() >= maxPlayers)
			return true;
		else
			return false;
	}
	
	private boolean overFlow()
	{
		if(players.size() > maxPlayers)
			return true;
		else
			return false;
	}
	
	
	public boolean playerInArena(Player player)
	{
		if(this.players.isEmpty())
			return false;
		if(this.players.contains(player))
			return true;
		else
			return false;
	}
	
	public String getArenaName()
	{
		return this.name;
	}
	
	public void setArenaName(String name)
	{
		this.name = name;
	}
	
	public List<Player> getPlayers()
	{
		return this.players;
	}
	
	public boolean addPlayer(Player player)
	{
		ArenaJoinEvent event = new ArenaJoinEvent(this, player);
		plugin.getServer().getPluginManager().callEvent(event);
			
		if(event.isCancelled())
			return false;
			
		this.players.add(player);
		return true;
	}
	
	public void setPlayers(List<Player> players)
	{
		for(Player p : players)
		{
			ArenaJoinEvent event = new ArenaJoinEvent(this, p);
			plugin.getServer().getPluginManager().callEvent(event);
			if(event.isCancelled())
			{
				continue;
			}else
			{
				this.players.add(p);
			}
		}
	}
	
	public void teleportPlayers()
	{
		if(overFlow())
		{
			for(int i = players.size() - 1; i > maxPlayers; i--)
			{
				players.get(i).sendMessage(ChatColor.RED + "Sorry this match is currently full you have been re-added to the queue");
				players.remove(i);
			}
		}
		
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).teleport(spawns.get(i));
		}
	}
	
	
	public void saveArena()
	{
		File folder = new File(plugin.getDataFolder(), "Arenas");
		if(!folder.exists())
		{
			folder.mkdirs();
		}
		File f = new File(folder, this.name + ".yml");
		if(!f.exists())
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
		fc.set("Arena.name", this.name);
		fc.set("Arena.maxPlayers", maxPlayers);
		int spawn = 0;
		for(Location l : spawns)
		{
			String path = "Arena.spawns.spawn" + spawn;
			fc.set(path + ".world", l.getWorld().getName());
			fc.set(path + ".x", l.getBlockX());
			fc.set(path + ".y", l.getBlockY());
			fc.set(path + ".z", l.getBlockZ());
			spawn++;
		}
		
		try {
			fc.save(f);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	

}

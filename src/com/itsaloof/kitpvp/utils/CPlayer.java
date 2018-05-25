package com.itsaloof.kitpvp.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;

public class CPlayer {
	Player p;
	KitPvPPlugin pl;
	private int kills;
	private int deaths;
	private File f;
	private boolean compMode;
	private int sp;
	private boolean registered;
	private String discordID;
	
	public CPlayer(Player player, KitPvPPlugin plugin)
	{
		p = player;
		pl = plugin;
		compMode = false;
		
		if(!checkPlayer(player))
		{
			f = new File(pl.getFolder(), player.getUniqueId() + ".yml");
			registered = false;
			discordID = "";
		}
	}
	
	private boolean checkPlayer(Player player)
	{
		if(pl.getFolder().listFiles().length > 0)
		{
			for(File f : pl.getFolder().listFiles())
			{
				if(f.getName().contains(player.getUniqueId().toString()))
				{
					this.f = f;
					loadData(f);
					return true;
				}else
				{
				continue;
				}
			}
			
			return false;
		}else
		{
		return false;
		}
	}
	
	private void loadData(File f)
	{
		FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
		setKills(fc.getInt("Player.stats.kills"));
		setDeaths(fc.getInt("Player.stats.deaths"));
		setSkillPoints(fc.getInt("Player.stats.skillpoints"));
		registered = fc.getBoolean("Player.discord.registered");
		discordID = fc.getString("Player.discord.ID");
	}
	
	public void register(String id)
	{
		this.registered = true;
		this.discordID = id;
		save();
	}
	
	public boolean isRegistered()
	{
		return registered;
	}
	
	public String getDiscordID()
	{
		return discordID;
	}
	
	public int getKills()
	{
		return kills;
	}
	
	public int getDeaths()
	{
		return deaths;
	}
	
	public void setDeaths(int newDeaths)
	{
		deaths = newDeaths;
	}
	
	public void setKills(int newKills)
	{
		kills = newKills;
	}
	
	public void setSkillPoints(int skillPoints)
	{
		sp = skillPoints;
	}
	
	public void addDeaths(int amount)
	{
		deaths += amount;
	}
	
	public void addKills(int amount)
	{
		kills += amount;
	}
	
	public void removeSkillPoints()
	{
		
	}
	
	public void addSkillPoints(CPlayer cp)
	{
		
	}
	
	public void save()
	{
		if(!pl.getDataFolder().exists())
		{
			pl.getDataFolder().mkdir();
		}
		if(!f.exists())
		{
			try {
				f = new File(pl.getFolder(), p.getUniqueId().toString() + ".yml");
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
		fc.createSection("Player.stats");
		fc.set("Player.stats.kills", kills);
		fc.set("Player.stats.deaths", deaths);
		fc.set("Player.stats.skillpoints", sp);
		fc.set("Player.name", p.getName());
		fc.set("Player.discord.registered", registered);
		fc.set("Player.discord.ID", discordID);
		try {
			fc.save(f);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
  
	public void toggleCompMode()
	{
		if(compMode)
		{
			compMode = false;
		}else
		{
			compMode = true;
		}
	}
	
	public boolean compMode()
	{
		return compMode;
	}
	
}

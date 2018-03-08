package com.itsaloof.kitpvp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.itsaloof.kitpvp.commands.ArenaCommand;
import com.itsaloof.kitpvp.listeners.JoinLeaveEvent;
import com.itsaloof.kitpvp.listeners.KillEvent;
import com.itsaloof.kitpvp.listeners.LaunchpadListener;
import com.itsaloof.kitpvp.listeners.SignEvent;
import com.itsaloof.kitpvp.utils.Arena;
import com.itsaloof.kitpvp.utils.ArenaBuilderUtil;
import com.itsaloof.kitpvp.utils.CPlayer;
import com.itsaloof.kitpvp.utils.LaunchpadUtils;

import net.milkbowl.vault.economy.Economy;

public class KitPvPPlugin extends JavaPlugin {

	public HashMap<Player, CPlayer> players = new HashMap<Player, CPlayer>();
	public List<Player> noFall = new ArrayList<Player>();
	public static Economy econ = null;
	public FileConfiguration config;
	public final LaunchpadUtils launchpadUtils = new LaunchpadUtils(this);
	public List<Player> queue = new ArrayList<Player>();
	public List<ArenaBuilderUtil> underConstruction = new ArrayList<ArenaBuilderUtil>();
	public List<Arena> arenas = new ArrayList<Arena>();
	private final String folderName = "PlayerData";
	private final String arenaFileName = "Arenas.yml";

	@Override
	public void onEnable()
	{
		createFiles();
		Bukkit.getPluginManager().registerEvents(new JoinLeaveEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new KillEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new SignEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new LaunchpadListener(this), this);
		
		getCommand("arena").setExecutor(new ArenaCommand(this));
		
		FileConfiguration fc = YamlConfiguration.loadConfiguration(getArenaFile());
		for(String s : fc.getConfigurationSection("Arenas").getKeys(false))
		{
			this.arenas.add(loadArena(s, fc));
			System.out.println("Successfull loaded arena " + arenas.get((arenas.size() - 1)));
		}
	}

	@Override
	public void onDisable()
	{
		for(Player p : players.keySet())
		{
			CPlayer cp = players.get(p);
			cp.save();
		}

		for(Arena a : arenas)
		{
			a.saveArena();
		}
	}

	public void createFiles()
	{
		config = getConfig();
		if(!getDataFolder().exists())
		{
			saveDefaultConfig();
			getDataFolder().mkdirs();
			File f = new File(getDataFolder(), folderName);
			File aFile = new File(getDataFolder(), arenaFileName);
			if(!aFile.exists())
			{
				try 
				{
					aFile.createNewFile();
					setupArenaFile(YamlConfiguration.loadConfiguration(aFile), aFile);
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			if(!f.exists())
				f.mkdir();
		}
		setupEconomy();

	}

	private boolean setupEconomy() 
	{
        if (getServer().getPluginManager().getPlugin("Vault") == null) 
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

	public File getFolder()
	{
		if(getDataFolder().exists())
		{
			return new File(getDataFolder(), folderName);
		}else
		{
			createFiles();
			return new File(getDataFolder(), folderName);
		}
	}
	
	public File getArenaFile()
	{
		File arenaFile = new File(getDataFolder(), arenaFileName);
		if(arenaFile.exists())
			return arenaFile;
		try {
			arenaFile.createNewFile();
			setupArenaFile(YamlConfiguration.loadConfiguration(arenaFile), arenaFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arenaFile;
	}
	
	private void setupArenaFile(FileConfiguration fc, File f)
	{
		fc.createSection("Arenas");
		try {
			fc.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Arena loadArena(final String name, final FileConfiguration fc)
	{
		Arena arena = new Arena(this);
		return arena.loadArena(name, fc);
	}
}

package com.itsaloof.kitpvp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.itsaloof.kitpvp.listeners.JoinLeaveEvent;
import com.itsaloof.kitpvp.listeners.KillEvent;
import com.itsaloof.kitpvp.listeners.LaunchpadListener;
import com.itsaloof.kitpvp.listeners.SignEvent;
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

	@Override
	public void onEnable()
	{
		createFiles();
		Bukkit.getPluginManager().registerEvents(new JoinLeaveEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new KillEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new SignEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new LaunchpadListener(this), this);
	}

	@Override
	public void onDisable()
	{
		for(Player p : players.keySet())
		{
			CPlayer cp = players.get(p);
			cp.save();
		}
	}

	public void createFiles()
	{
		config = getConfig();
		if(!getDataFolder().exists())
		{
			saveDefaultConfig();
			getDataFolder().mkdirs();
			File f = new File(getDataFolder(), "PlayerData");
			if(!f.exists())
			{
				f.mkdir();
			}
		}
		setupEconomy();

	}

	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
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
			return new File(getDataFolder(), "PlayerData");
		}else
		{
			createFiles();
			return new File(getDataFolder(), "PlayerData");
		}
	}
}

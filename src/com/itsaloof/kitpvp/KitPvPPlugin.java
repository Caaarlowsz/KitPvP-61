package com.itsaloof.kitpvp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.itsaloof.kitpvp.commands.ArenaCommand;
import com.itsaloof.kitpvp.commands.RegisterCommand;
import com.itsaloof.kitpvp.commands.discord.BalanceCommand;
import com.itsaloof.kitpvp.commands.discord.BaltopCommand;
import com.itsaloof.kitpvp.commands.discord.ChooseCommand;
import com.itsaloof.kitpvp.commands.discord.HelloCommand;
import com.itsaloof.kitpvp.commands.discord.KitPvPStatsCommand;
import com.itsaloof.kitpvp.commands.discord.ListCommand;
import com.itsaloof.kitpvp.commands.discord.RegisterDiscordCommand;
import com.itsaloof.kitpvp.listeners.JoinLeaveEvent;
import com.itsaloof.kitpvp.listeners.KillEvent;
import com.itsaloof.kitpvp.listeners.LaunchpadListener;
import com.itsaloof.kitpvp.listeners.SignEvent;
import com.itsaloof.kitpvp.listeners.discord.ChannelCreationListener;
import com.itsaloof.kitpvp.utils.Arena;
import com.itsaloof.kitpvp.utils.ArenaBuilderUtil;
import com.itsaloof.kitpvp.utils.CPlayer;
import com.itsaloof.kitpvp.utils.LaunchpadUtils;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
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
	
	public HashMap<UUID, HashMap<Guild, User>> registration = new HashMap<UUID, HashMap<Guild, User>>();
	
	public JDA api;
	//private ClientConnection client;
	
	public static String IDpath = "Player.discord.ID";
	public static String Registeredpath = "Player.discord.registered";

	@Override
	public void onEnable()
	{
		createFiles();
		Bukkit.getPluginManager().registerEvents(new JoinLeaveEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new KillEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new SignEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new LaunchpadListener(this), this);
		
		getCommand("arena").setExecutor(new ArenaCommand(this));
		getCommand("register").setExecutor(new RegisterCommand(this));
		
		FileConfiguration fc = YamlConfiguration.loadConfiguration(getArenaFile());
		for(String s : fc.getConfigurationSection("Arenas").getKeys(false))
		{
			this.arenas.add(loadArena(s, fc));
			System.out.println("Successfull loaded arena " + arenas.get((arenas.size() - 1)));
		}
		
		CommandClientBuilder builder = new CommandClientBuilder();
		builder.addCommands(new HelloCommand(new EventWaiter()), new ChooseCommand(),
				new ListCommand(this),
				new BaltopCommand(this),
				new RegisterDiscordCommand(this),
				new BalanceCommand(this),
				new KitPvPStatsCommand(this));
		builder.setPrefix("?");
		builder.setGame(Game.playing("Use ?help to see all commands and info"));
		builder.setOwnerId("192730242673016832");
		builder.build();
		try {
			api = new JDABuilder(AccountType.BOT)
					.setToken(config.getString("bot-token"))
					.setGame(Game.playing("Loading..."))
					.buildAsync();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		api.addEventListener(builder.build());
		api.addEventListener(new ChannelCreationListener(this));
		
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
	
	public static String getUniqueTag(User user)
	{
		return user.getName() + "#" + user.getDiscriminator();
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
	

	public File getUser(String tag)
	{
		for(File f : getFolder().listFiles())
		{
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			if(fc.getString(KitPvPPlugin.IDpath).trim().equals(tag.trim()))
			{
				return f;
			}
		}
		return null;
	}
}

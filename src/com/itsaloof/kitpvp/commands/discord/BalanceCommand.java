package com.itsaloof.kitpvp.commands.discord;

import java.io.File;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BalanceCommand extends Command {
	KitPvPPlugin plugin;
	public BalanceCommand(KitPvPPlugin plugin)
	{
		this.name = "balance";
		this.aliases = new String[] {"bal"};
		this.help = "Displays current balance on MC server";
		this.requiredRole = "registered";
		this.plugin = plugin;
	}
	
	@Override
	protected void execute(CommandEvent event)
	{
		File f = getUser(KitPvPPlugin.getUniqueTag(event.getAuthor()));
		if(f != null)
		{
			OfflinePlayer p = plugin.getServer().getOfflinePlayer(UUID.fromString(f.getName().replace(".yml", "")));
			event.getChannel().sendMessage(p.getName() + "'s Balance: $" +
					KitPvPPlugin.econ.getBalance(p)).queue();
			return;
		}
	}
	
	private File getUser(String tag)
	{
		for(File f : plugin.getFolder().listFiles())
		{
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			if(fc.getString(KitPvPPlugin.IDpath).trim().equals(tag.trim()))
			{
				return f;
			}
			System.out.println(fc.getString(KitPvPPlugin.IDpath) + " != " + tag);
		}
		return null;
	}
	

}

package com.itsaloof.kitpvp.commands.discord;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.utils.CPlayer;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class KitPvPStatsCommand extends Command {
	
	KitPvPPlugin plugin;
	
	public KitPvPStatsCommand(KitPvPPlugin plugin) 
	{
		this.plugin = plugin;
		this.name = "stats";
		this.requiredRole = "registered";
		this.help = "Gives the stats on a registered players MC account for KitPvP";
	}
	
	@Override
	protected void execute(CommandEvent event) 
	{
		File f = plugin.getUser(KitPvPPlugin.getUniqueTag(event.getAuthor()));
		CPlayer player = new CPlayer(f, plugin);
		FileConfiguration fc = player.getPlayerConfig();
		String s = fc.getString("Player.name") + "'s Stats" +
		"\nKills: " + player.getKills() +
		"\nDeaths: " + player.getDeaths() +
		"\nK/D: " + ((player.getDeaths() == 0 ? (player.getKills()) : (player.getKills()/player.getDeaths()))) +
		"\nSkillPoints: " + player.getSkillPoints();
		
		event.getChannel().sendMessage(s).queue();
	}

}

package com.itsaloof.kitpvp.commands.discord;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.OfflinePlayer;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BaltopCommand extends Command{
	KitPvPPlugin plugin;
	public BaltopCommand(KitPvPPlugin plugin)
	{
		this.name = "baltop";
		this.aliases = new String[] {"btop"};
		this.help = "Gives top ten balances on MC Server";
		this.plugin = plugin;
	}
	
	@Override
	protected void execute(CommandEvent event) 
	{
		Map<Double, String> baltop = new TreeMap<Double, String>(Collections.reverseOrder());
		for(OfflinePlayer p : plugin.getServer().getOfflinePlayers())
		{
			baltop.put(KitPvPPlugin.econ.getBalance(p), p.getName());
		}
		String s = "<--- Baltop --->";
		int i = 1;
		for(double d : baltop.keySet())
		{
			if(i > 10)
			{
				event.getChannel().sendMessage(s).queue();
				return;
			}	
			s += "\n" + i + ".) " + baltop.get(d) + " $" + d;
			i++;
		}
		event.getChannel().sendMessage(s).queue();
		return;
	}

}

package com.itsaloof.kitpvp.commands.discord;

import org.bukkit.entity.Player;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ListCommand extends Command{
	KitPvPPlugin plugin;
	public ListCommand(KitPvPPlugin plugin)
	{
		this.plugin = plugin;
		this.name = "list";
		this.help = "Lists all current players on MC server";
	}
	
	@Override
	protected void execute(CommandEvent event)
	{
		if(plugin.getServer().getOnlinePlayers().size() == 0)
		{
			event.getChannel().sendMessage("There are currently no players online!").queue();
			return;
		}
		String s = "<= Players Online =>";
		for(Player p : plugin.getServer().getOnlinePlayers())
		{
			s += "\n" + p.getName();
		}
		event.getChannel().sendMessage(s).queue();
		return;
	}

}

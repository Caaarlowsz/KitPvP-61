package com.itsaloof.kitpvp.commands.discord;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class HelpCommand  extends Command {

	public HelpCommand(KitPvPPlugin plugin)
	{
		this.name = "help";
		this.help = "Help command shows all commands for bot.";
		
	}
	
	@Override
	protected void execute(CommandEvent event) 
	{
		
	}

}

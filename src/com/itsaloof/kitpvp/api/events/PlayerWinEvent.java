package com.itsaloof.kitpvp.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.itsaloof.kitpvp.utils.Arena;


public class PlayerWinEvent extends Event implements Cancellable
{
	private final Arena arena;
	private final Player winner;
	private boolean cancelled = false;
	
	public PlayerWinEvent(Arena arena, Player winner)
	{
		this.arena = arena;
		this.winner = winner;
	}
	
	public Player getWinner()
	{
		return winner;
	}
	
	public Arena getArena()
	{
		return arena;
	}
	
	public Player getLoser()
	{
		for(Player p : arena.getPlayers())
		{
			if(p != winner)
				return p;
		}
		return null;
	}
	
	@Override
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	

	@Override
	public boolean isCancelled() {
		return cancelled;
	}


	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return null;
	}
	

}

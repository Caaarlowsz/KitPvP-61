package com.itsaloof.kitpvp.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.itsaloof.kitpvp.utils.Arena;


public class PlayerWinEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
	private final Arena arena;
	private final Player winner;
	private final Player loser;
	private boolean cancelled = false;
	
	public PlayerWinEvent(Arena arena, Player winner, Player loser)
	{
		this.arena = arena;
		this.winner = winner;
		this.loser = loser;
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
		return loser;
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


	public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
	public HandlerList getHandlers() {
        return handlers;
    }
	

}

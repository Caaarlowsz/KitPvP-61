package com.itsaloof.kitpvp.launch.events;

import com.itsaloof.kitpvp.launch.utils.LaunchPhase;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class LaunchEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LaunchPhase launchPhase;
    private boolean cancelled = false;

    public LaunchEvent(final Player player, final LaunchPhase launchPhase) {
        super(player);
        this.launchPhase = launchPhase;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public LaunchPhase getLaunchPhase() {
        return this.launchPhase;
    }
}

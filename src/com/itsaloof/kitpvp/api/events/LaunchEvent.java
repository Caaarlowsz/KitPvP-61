package com.itsaloof.kitpvp.api.events;

import com.itsaloof.kitpvp.api.enums.LaunchPhase;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class LaunchEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LaunchPhase launchPhase;
    private boolean cancelled;

    public LaunchEvent(final Player player, final LaunchPhase launchPhase) {
        super(player);
        this.launchPhase = launchPhase;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public LaunchPhase getLaunchPhase() {
        return this.launchPhase;
    }
}

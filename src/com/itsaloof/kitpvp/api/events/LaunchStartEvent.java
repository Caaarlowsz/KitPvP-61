package com.itsaloof.kitpvp.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LaunchStartEvent extends LaunchEvent {
    private static final HandlerList handlers = new HandlerList();

    public LaunchStartEvent(final Player player) {
        super(player);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

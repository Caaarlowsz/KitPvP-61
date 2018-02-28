package com.itsaloof.kitpvp.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LaunchEndEvent extends LaunchEvent {
    private static final HandlerList handlers = new HandlerList();

    public LaunchEndEvent(final Player player) {
        super(player);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

package com.itsaloof.kitpvp.utils;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class JumpPads {
    Player player;

    public JumpPads(Player player) {
        this.player = player;
    }

    public void launch() {
        player.setVelocity(new Vector(player.getVelocity().getBlockX() * 6, player.getVelocity().getBlockY() * 6, player.getVelocity().getBlockZ() * 6));
    }


}

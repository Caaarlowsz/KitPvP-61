package com.itsaloof.kitpvp.launch.utils;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.launch.events.LaunchEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LaunchCheckTask extends BukkitRunnable {
    private final KitPvPPlugin plugin;
    private final LaunchEvent event;

    public LaunchCheckTask(final KitPvPPlugin plugin, LaunchEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    @Override
    public void run() {
        final Player player = event.getPlayer();
        if (!this.plugin.launchUtils.isOnLaunchpad(player) && (((Entity) player).isOnGround() || LaunchUtils.getAdditionalGroundMaterials().contains(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()))) {
            this.plugin.launchUtils.endLaunch(player);
            this.cancel();
        }
    }

    public void begin() {
        this.runTaskTimer(this.plugin, 0L, this.plugin.launchUtils.getCheckInterval());
    }
}

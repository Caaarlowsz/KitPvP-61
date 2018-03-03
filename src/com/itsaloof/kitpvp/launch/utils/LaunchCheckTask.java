package com.itsaloof.kitpvp.launch.utils;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.launch.events.LaunchEvent;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class LaunchCheckTask extends BukkitRunnable {
    private static final List<Material> additionalGroundMaterials = Arrays.asList(
            Material.LADDER,
            Material.LAVA,
            Material.SLIME_BLOCK,
            Material.STATIONARY_LAVA,
            Material.STATIONARY_WATER,
            Material.VINE,
            Material.WATER
    );
    private final KitPvPPlugin plugin;
    private final LaunchEvent event;

    public LaunchCheckTask(final KitPvPPlugin plugin, LaunchEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    @Override
    public void run() {
        final Player player = event.getPlayer();
        if (!this.plugin.launchUtils.isOnLaunchpad(player)) {
            final Material material = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
            if (!(material == Material.SLIME_BLOCK && additionalGroundMaterials.contains(Material.SLIME_BLOCK) && !player.isSneaking()) && (((Entity) player).isOnGround() || additionalGroundMaterials.contains(material))) {
                this.plugin.launchUtils.endLaunch(player);
                this.cancel();
            }
        }
    }

    public void begin() {
        this.runTaskTimer(this.plugin, 0L, this.plugin.launchUtils.getCheckInterval());
    }
}

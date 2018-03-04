package com.itsaloof.kitpvp.launch.utils;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.launch.events.LaunchEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LaunchCheckTask extends BukkitRunnable {
    private static final List<Material> collisionMaterials = Arrays.asList(
            Material.LADDER,
            Material.LAVA,
            Material.STATIONARY_LAVA,
            Material.STATIONARY_WATER,
            Material.VINE,
            Material.WATER
    );
    private final KitPvPPlugin plugin;
    private final Player player;
    private final Entity playerEntity;
    private final UUID uuid;
    private boolean bouncingLow = false;

    public LaunchCheckTask(final KitPvPPlugin plugin, final LaunchEvent event) {
        this.plugin = plugin;
        this.player = event.getPlayer();
        this.playerEntity = (Entity) this.player;
        this.uuid = this.player.getUniqueId();
    }

    @Override
    public void run() {
        if (this.plugin.launchUtils.isBeingLaunched(this.uuid)) {
            if (this.plugin.launchUtils.isOnLaunchpad(this.uuid)) {
                return;
            }

            final Block containingBlock = this.player.getLocation().getBlock();
            final Material containingMaterial = containingBlock.getType();
            final Material belowMaterial = containingBlock.getRelative(BlockFace.DOWN).getType();

            if (belowMaterial == Material.SLIME_BLOCK) {
                if (this.player.getVelocity().getY() > 0.0) {
                    if (!this.bouncingLow) {
                        this.bouncingLow = true;
                    }

                    return;
                } else if (!this.bouncingLow) {
                    return;
                }
            } else {
                if (this.bouncingLow) {
                    this.bouncingLow = false;
                }

                if (!this.playerEntity.isOnGround() && !collisionMaterials.contains(containingMaterial)) {
                    return;
                }
            }

            this.plugin.launchUtils.endLaunch(this.player, this.uuid);
        }

        this.cancel();
    }

    public void begin() {
        this.runTaskTimer(this.plugin, 0L, this.plugin.launchUtils.getCheckInterval());
    }
}

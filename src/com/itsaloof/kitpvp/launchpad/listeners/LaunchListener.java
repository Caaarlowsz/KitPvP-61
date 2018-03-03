package com.itsaloof.kitpvp.launchpad.listeners;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.launchpad.utils.LaunchUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class LaunchListener implements Listener {
    private final KitPvPPlugin plugin;

    public LaunchListener(final KitPvPPlugin plugin) {
        this.plugin = plugin;
    }

    /* The player's y-velocity cannot be set with this event unless they are falling into the launchpad.
    @EventHandler
    public void onTriggerLaunch(final PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == this.plugin.launchUtils.getMaterial()) {
            this.plugin.launchUtils.startLaunch(event.getPlayer());
        }
    }
    */

    @EventHandler
    public void onTriggerLaunch(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getFrom().getBlock();
        final Material material = this.plugin.launchUtils.getMaterial();

        if (block.getType() == material || block.getRelative(BlockFace.UP).getType() == material) {
            this.plugin.launchUtils.setOnLaunchpad(player, true);
            this.plugin.launchUtils.startLaunch(player);
        } else {
            this.plugin.launchUtils.setOnLaunchpad(player, false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFlightToggledOnDuringLaunch(final PlayerToggleFlightEvent event) {
        if (event.isFlying()) {
            this.plugin.launchUtils.endLaunch(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleportDuringLaunch(final PlayerTeleportEvent event) {
        this.plugin.launchUtils.endLaunch(event.getPlayer());
    }

    @EventHandler
    public void onDeathDuringLaunch(final PlayerDeathEvent event) {
        this.plugin.launchUtils.endLaunch(event.getEntity());
    }

    @EventHandler
    public void onDisconnectDuringLaunch(final PlayerQuitEvent event) {
        this.plugin.launchUtils.endLaunch(event.getPlayer());
    }

    @EventHandler
    public void onFallDamageFromLaunch(final EntityDamageEvent event) {
        if (event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (this.plugin.launchUtils.endLaunch(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onNoFallDamageFromLaunch(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!this.plugin.launchUtils.isOnLaunchpad(player) && (((Entity) player).isOnGround() || LaunchUtils.getAdditionalGroundMaterials().contains(event.getFrom().getBlock().getRelative(BlockFace.DOWN).getType()))) {
            this.plugin.launchUtils.endLaunch(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLaunchpadSupportRemoved(final BlockPhysicsEvent event) {
        if (event.getBlock().getType() == this.plugin.launchUtils.getMaterial()) {
            event.setCancelled(true);
        }
    }
}

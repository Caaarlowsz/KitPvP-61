package com.itsaloof.kitpvp.launch.utils;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.launch.events.LaunchEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LaunchUtils {
    private final KitPvPPlugin plugin;
    private final List<UUID> onLaunchpad = new ArrayList<>();
    private final List<UUID> beingLaunched = new ArrayList<>();

    public LaunchUtils(final KitPvPPlugin plugin) {
        this.plugin = plugin;
    }

    public Material getMaterial() {
        return Material.getMaterial(this.plugin.getConfig().getString("launch.launchpad-material").toUpperCase());
    }

    public double getMultiplier() {
        return this.plugin.getConfig().getDouble("launch.velocity-multiplier");
    }

    public long getCheckInterval() {
        return this.plugin.getConfig().getLong("launch.launch-check-tick-interval");
    }

    public boolean isOnLaunchpad(final UUID uuid) {
        return this.onLaunchpad.contains(uuid);
    }

    public boolean isOnLaunchpad(final Player player) {
        return this.isOnLaunchpad(player.getUniqueId());
    }

    public void setOnLaunchpad(final UUID uuid, boolean onLaunchpad) {
        if (onLaunchpad) {
            this.onLaunchpad.add(uuid);
        } else {
            this.onLaunchpad.remove(uuid);
        }
    }

    public void setOnLaunchpad(final Player player, boolean onLaunchpad) {
        this.setOnLaunchpad(player.getUniqueId(), onLaunchpad);
    }

    public boolean isBeingLaunched(final UUID uuid) {
        return this.beingLaunched.contains(uuid);
    }

    public boolean isBeingLaunched(final Player player) {
        return this.isBeingLaunched(player.getUniqueId());
    }

    private void setBeingLaunched(final UUID uuid, boolean beingLaunched) {
        if (beingLaunched) {
            this.beingLaunched.add(uuid);
        } else {
            this.beingLaunched.remove(uuid);
        }
    }

    private void setBeingLaunched(final Player player, boolean beingLaunched) {
        this.setBeingLaunched(player.getUniqueId(), beingLaunched);
    }

    public boolean startLaunch(final Player player, final UUID uuid) {
        if (this.isBeingLaunched(uuid)) {
            return false;
        }

        final LaunchEvent event = new LaunchEvent(player, LaunchPhase.START);
        this.plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        player.setVelocity(player.getEyeLocation().getDirection().multiply(this.getMultiplier()));
        this.setBeingLaunched(uuid, true);
        final LaunchCheckTask launchCheckTask = new LaunchCheckTask(this.plugin, event);
        launchCheckTask.begin();
        return true;
    }

    public boolean startLaunch(final Player player) {
        return this.startLaunch(player, player.getUniqueId());
    }

    public boolean startLaunch(final UUID uuid) {
        return this.startLaunch(this.plugin.getServer().getPlayer(uuid), uuid);
    }

    public boolean endLaunch(final Player player, final UUID uuid) {
        if (!this.isBeingLaunched(uuid)) {
            return false;
        }

        final LaunchEvent event = new LaunchEvent(player, LaunchPhase.END);
        this.plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        this.setBeingLaunched(uuid, false);
        return true;
    }

    public boolean endLaunch(final Player player) {
        return this.endLaunch(player, player.getUniqueId());
    }

    public boolean endLaunch(final UUID uuid) {
        return this.endLaunch(this.plugin.getServer().getPlayer(uuid), uuid);
    }
}

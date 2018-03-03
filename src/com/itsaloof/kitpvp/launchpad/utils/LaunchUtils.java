package com.itsaloof.kitpvp.launchpad.utils;

import com.itsaloof.kitpvp.launchpad.enums.LaunchPhase;
import com.itsaloof.kitpvp.launchpad.events.LaunchEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LaunchUtils {
    private static final List<Material> additionalGroundMaterials = Arrays.asList(
            Material.LADDER,
            Material.LAVA,
            Material.STATIONARY_LAVA,
            Material.STATIONARY_WATER,
            Material.VINE,
            Material.WATER
            /* TODO: Add choice to include slime blocks in this list */

    );
    private final JavaPlugin plugin;
    private final List<UUID> onLaunchpad = new ArrayList<>();
    private final List<UUID> beingLaunched = new ArrayList<>();

    public LaunchUtils(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static List<Material> getAdditionalGroundMaterials() {
        return additionalGroundMaterials;
    }

    public Material getMaterial() {
        return Material.getMaterial(this.plugin.getConfig().getString("launchpad.material").toUpperCase());
    }

    public double getMultiplier() {
        return this.plugin.getConfig().getDouble("launchpad.multiplier");
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

    public boolean startLaunch(final Player player) {
        final UUID uuid = player.getUniqueId();
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
        return true;
    }

    public boolean endLaunch(final Player player) {
        final UUID uuid = player.getUniqueId();
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
}

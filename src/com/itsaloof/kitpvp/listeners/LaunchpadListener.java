package com.itsaloof.kitpvp.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.itsaloof.kitpvp.KitPvPPlugin;

public class LaunchpadListener implements Listener {
    private final KitPvPPlugin plugin;

    public LaunchpadListener(final KitPvPPlugin plugin) {
        this.plugin = plugin;
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
	  public void onPlayerMove(PlayerMoveEvent event)
	  {
    	Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        Material ID = playerLoc.getWorld().getBlockAt(playerLoc).getRelative(0, -1, 0).getType();
        Material plate = playerLoc.getWorld().getBlockAt(playerLoc).getType();
	    if ((player instanceof Player))
	    {
	      if (ID == Material.getMaterial(plugin.getConfig().getInt("launchpad.type"))) 
	      {
	      if (plate == Material.getMaterial(plugin.getConfig().getString("launchpad.plate")))
	      	{
	    	  player.setVelocity(player.getLocation().getDirection().multiply(plugin.getConfig().getInt("launchpad.strength")));
		      player.setVelocity(new Vector(player.getVelocity().getX(), plugin.getConfig().getDouble("launchpad.height"), player.getVelocity().getZ()));
		      if(!plugin.noFall.contains(player))
		      {
		    	  this.plugin.launchpadUtils.setOnLaunchpad(player, true);
		    	  this.plugin.launchpadUtils.startLaunch(player);
		    	  plugin.noFall.add(player);
		    	  timer(player);
		      }
	      	}
	      
	      }
	    }
	  }
    
    private void timer(Player p)
	 {
		 BukkitRunnable br = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(plugin.noFall.contains(p))
				{
					plugin.noFall.remove(p);
					return;
				}
				else
					return;
			}
		};
		br.runTaskLater(plugin, 100L);
}

    @EventHandler(ignoreCancelled = true)
    public void onFlightToggledOnDuringLaunch(final PlayerToggleFlightEvent event) {
        if (event.isFlying()) {
            this.plugin.launchpadUtils.endLaunch(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleportDuringLaunch(final PlayerTeleportEvent event) {
        this.plugin.launchpadUtils.endLaunch(event.getPlayer());
    }

    @EventHandler
    public void onDeathDuringLaunch(final PlayerDeathEvent event) {
        this.plugin.launchpadUtils.endLaunch(event.getEntity());
    }

    @EventHandler
    public void onDisconnectDuringLaunch(final PlayerQuitEvent event) {
        this.plugin.launchpadUtils.endLaunch(event.getPlayer());
    }

    @EventHandler
    public void onFallDamageFromLaunch(final EntityDamageEvent event) {
        if (event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (this.plugin.launchpadUtils.endLaunch(player)) {
                event.setCancelled(true);
            }
        }
    }
    
}

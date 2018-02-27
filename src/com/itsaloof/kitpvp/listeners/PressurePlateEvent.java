package com.itsaloof.kitpvp.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.itsaloof.kitpvp.KitPvPPlugin;

public class PressurePlateEvent implements Listener{
	
	KitPvPPlugin pl;
	
	public PressurePlateEvent(KitPvPPlugin plugin)
	{
		pl = plugin;
	}

	
	 @SuppressWarnings("deprecation")
	@EventHandler
	  public void onPlayerMove(PlayerMoveEvent event)
	  {
	    Player player = event.getPlayer();
	    Location playerLoc = player.getLocation();
	    Block ID = playerLoc.getWorld().getBlockAt(playerLoc).getRelative(0, -1, 0);
	    Material plate = playerLoc.getWorld().getBlockAt(playerLoc).getType();
	    if ((player instanceof Player))
	    {
	    	byte b = ID.getState().getRawData();
	      if (ID.getType() == Material.getMaterial(pl.getConfig().getString("launchpad.type")) && (b == 1 || b == 4 || b == 9 || b == 13)) 
	      {
	      if (plate == Material.getMaterial(pl.getConfig().getString("launchpad.plate")))
	      	{
	    	  player.setVelocity(player.getLocation().getDirection().multiply(pl.getConfig().getInt("launchpad.strength")));
		      player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
		      if(!pl.noFall.contains(player))
		    	  pl.noFall.add(player);
		      timer(player);
	      	}
	      
	      }
	    }
	  }
	 
	 private void timer(Player p)
	 {
		 BukkitRunnable br = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(pl.noFall.contains(p))
				{
					pl.noFall.remove(p);
					return;
				}
				else
					return;
			}
		};
		br.runTaskLater(pl, 100L);
	 }
	 
	 @EventHandler
	 public void onFall(EntityDamageEvent e)
	 {
		 if(e.getEntity() instanceof Player)
		 {
			 Player p = (Player) e.getEntity();
			 if(e.getCause() == DamageCause.FALL)
			 {
				 if(pl.noFall.contains(p))
				 {
					 e.setCancelled(true);
					 pl.noFall.remove(p);
					 return;
				 }
			 }
		 }
	 }

}

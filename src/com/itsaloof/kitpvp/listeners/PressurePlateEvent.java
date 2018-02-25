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
	      if (ID.getType() == Material.getMaterial(pl.getConfig().getString("launchpad.type")) && ID.getState().getRawData() >= 0 && ID.getState().getRawData() <= 3) 
	      {
	      if (plate == Material.getMaterial(pl.getConfig().getString("launchpad.plate")))
	      	{
	    	  player.setVelocity(player.getLocation().getDirection().multiply(pl.getConfig().getInt("launchpad.strength")));
		      player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
		      if(!pl.noFall.contains(player))
		    	  pl.noFall.add(player);
	      	}
	      
	      }
	    }
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

package com.itsaloof.kitpvp.listeners;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.utils.CPlayer;

import net.md_5.bungee.api.ChatColor;

public class SignEvent
  implements Listener
{
  KitPvPPlugin plugin;
  
  public SignEvent(KitPvPPlugin plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onSign(SignChangeEvent e)
  {
    if ((e.getLine(0).equalsIgnoreCase("[competitive]")) && (e.getPlayer().hasPermission("kitpvp.sign.create")))
    {
      e.setLine(1, ChatColor.GREEN + e.getLine(1));
      e.setLine(0, "§8[§6Competitive§8]");
      e.setLine(1, "Queued: " + this.plugin.queue.size());
      this.plugin.signs.add(e.getBlock().getLocation());
    }
    else {}
  }
  
  @EventHandler
  public void onRightClick(PlayerInteractEvent event)
  {
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if (event.getClickedBlock().getType().toString().toLowerCase().contains("sign"))
      {
        Sign s = (Sign)event.getClickedBlock().getState();
        if (s.getLine(0).equals("§8[§6Competitive§8]"))
        {
          if (!this.plugin.signs.contains(event.getClickedBlock().getLocation())) {
            this.plugin.signs.add(event.getClickedBlock().getLocation());
          }
          if (!this.plugin.queue.contains(this.plugin.players.get(event.getPlayer())))
          {
            this.plugin.queue.add((CPlayer)this.plugin.players.get(event.getPlayer()));
            event.getPlayer().sendMessage(ChatColor.GREEN + "You are now in queue");
          }
        }
      }
    }
    this.plugin.updateSigns();
  }
  
  @EventHandler
  public void onLeftClick(PlayerInteractEvent event)
  {
    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
      if (event.getClickedBlock().getType().toString().toLowerCase().contains("sign"))
      {
        Sign s = (Sign)event.getClickedBlock().getState();
        if (s.getLine(0).equals("§8[§6Competitive§8]")) {
          if (this.plugin.queue.contains(this.plugin.players.get(event.getPlayer())))
          {
            this.plugin.queue.remove(this.plugin.players.get(event.getPlayer()));
            event.getPlayer().sendMessage(ChatColor.RED + "You are no longer in queue");
          }
          if(!event.getPlayer().isSneaking())
          {
        	  event.setCancelled(true);
          }
        }
      }
    }
    this.plugin.updateSigns();
  }
}

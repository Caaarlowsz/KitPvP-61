package com.itsaloof.kitpvp.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class ArenaSpawnVisual
  extends BukkitRunnable
{
  private List<Location> spawns;
  private Player player;
  public boolean isRunning = false;
  
  public ArenaSpawnVisual(List<Location> locs, Player creator)
  {
    this.spawns = locs;
    this.player = creator;
  }
  
  public List<Location> getSpawns()
  {
    return this.spawns;
  }
  
  public void setSpawns(List<Location> locs)
  {
    this.spawns = locs;
  }
  
  public void setPlayer(Player player)
  {
    this.player = player;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public void run()
  {
    for (Location l : this.spawns)
    {
      for(int i = 0; i <= 10; i++)
      {
      PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, l.getBlockX(), l.getBlockY() + i, l.getBlockZ(), 
        0.0F, 0.0F, 0.0F, 0.0F, 10, new int[] { 0 });
      ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }
}

package com.itsaloof.kitpvp.listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.api.events.ArenaJoinEvent;
import com.itsaloof.kitpvp.api.events.PlayerWinEvent;
import com.itsaloof.kitpvp.utils.Arena;
import com.itsaloof.kitpvp.utils.CPlayer;

public class ArenaListener implements Listener {
	private final KitPvPPlugin plugin;

	public ArenaListener(KitPvPPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onArenaJoin(ArenaJoinEvent event) {
		Arena a = event.getArena();
		if (!event.isCancelled()) {
			CPlayer cp = plugin.players.get(event.getPlayer());
			cp.setInventory(event.getPlayer().getInventory().getContents());
			cp.getPlayer().getInventory().clear();
			cp.setCurrentArena(a);
		}
	}

	@EventHandler
	public void onPlayerWin(PlayerWinEvent event) {
		Arena a = event.getArena();
		a.toggleInUse();
		teleportPlayers(a.getPlayers());
		a.clearArena();
		event.getWinner().getInventory()
				.setContents(((CPlayer) this.plugin.players.get(event.getWinner())).getInventory());
		event.getLoser().getInventory()
				.setContents(((CPlayer) this.plugin.players.get(event.getLoser())).getInventory());
		
		((CPlayer) plugin.players.get(event.getWinner())).setCurrentArena(null);
		((CPlayer) plugin.players.get(event.getLoser())).setCurrentArena(null);
		plugin.getServer()
				.broadcastMessage("§a" + event.getWinner().getName() + " §fhas beaten §4" + event.getLoser().getName());
	}

	private void teleportPlayers(List<Player> players) {
		for (Player p : players) {
			{
				p.teleport(p.getLocation().getWorld().getSpawnLocation());
				p.resetMaxHealth();
				p.setFoodLevel(20);
			}
		}
	}
	
	

	@EventHandler
	public void onDeath(EntityDamageByEntityEvent event) {
		if ((event.getEntity() instanceof Player)) {
			if ((event.getDamager() instanceof Player)) {
				if (((Player) event.getEntity()).getHealth() - event.getDamage() <= 0) {
					Player winner = (Player) event.getDamager();
					Player loser = (Player) event.getEntity();
					PlayerWinEvent e = new PlayerWinEvent(plugin.players.get(winner).getCurrentArena(),
							winner, (Player) event.getEntity());
					this.plugin.getServer().getPluginManager().callEvent(e);
					if(!e.isCancelled())
					{
						event.setCancelled(true);
						winner.setHealth(20.0);
						loser.setHealth(20.0);
					}
				}
			}
		}
	}
}

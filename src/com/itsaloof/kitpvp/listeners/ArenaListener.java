package com.itsaloof.kitpvp.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

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
			cp.setInventory(event.getPlayer().getInventory());
			cp.getPlayer().getInventory().clear();
			cp.setCurrentArena(a);
			plugin.queue.remove(cp);
			plugin.updateSigns();

		}
	}

	@EventHandler
	public void onPlayerWin(PlayerWinEvent event) {
		Arena a = event.getArena();
		teleportPlayers(a.getPlayers());
		a.toggleInUse();
		a.setPlayers(new ArrayList<Player>());
		event.getWinner().getInventory()
				.setContents(((CPlayer) this.plugin.players.get(event.getWinner())).getInventory().getContents());
		event.getLoser().getInventory()
				.setContents(((CPlayer) this.plugin.players.get(event.getWinner())).getInventory().getContents());
		((CPlayer) plugin.players.get(event.getWinner())).setCurrentArena(null);
		((CPlayer) plugin.players.get(event.getLoser())).setCurrentArena(null);
		plugin.getServer().broadcastMessage("§a" + event.getWinner().getName() + " §fhas beaten §4" + event.getLoser().getName());
	}
	
	private void teleportPlayers(List<Player> players)
	{
		for(Player p : players)
		{
			p.teleport(p.getLocation().getWorld().getSpawnLocation());
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if ((event.getEntity() instanceof Player)) {
			if ((event.getEntity().getKiller() instanceof Player)) {
				Player winner = event.getEntity().getKiller();
				PlayerWinEvent e = new PlayerWinEvent(((CPlayer) this.plugin.players.get(winner)).getCurrentArena(),
						winner,(Player) event.getEntity());
				this.plugin.getServer().getPluginManager().callEvent(e);
			}
		}
	}
}

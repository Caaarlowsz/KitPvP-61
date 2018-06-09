package com.itsaloof.kitpvp.utils;

import org.bukkit.scheduler.BukkitRunnable;

import com.itsaloof.kitpvp.KitPvPPlugin;

public class MatchMaker extends BukkitRunnable {
	KitPvPPlugin plugin;

	public MatchMaker(KitPvPPlugin plugin) {
		this.plugin = plugin;
	}

		@Override
		public void run() {
			if (plugin.queue.size() >= 2) {
				for (Arena a : plugin.arenas) {
					if (!a.inUse()) {
						for (CPlayer p : plugin.queue) {
							if (!a.isArenaFull()) {
								a.addPlayer(p.getPlayer());
							} else {
								return;
							}
						}
					}
				}
			}
		}
}

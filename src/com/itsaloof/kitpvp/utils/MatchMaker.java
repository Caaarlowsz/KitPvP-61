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
					if (!a.inUse() && !a.isArenaFull() && !plugin.queue.isEmpty()) {
						for(int i = 0; i < plugin.queue.size(); i++)
						{
							CPlayer p = plugin.queue.get(i);
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

package com.itsaloof.kitpvp.listeners;

import com.itsaloof.kitpvp.KitPvPPlugin;
import com.itsaloof.kitpvp.utils.CPlayer;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

public class KillEvent implements Listener {

    KitPvPPlugin pl;

    public KillEvent(KitPvPPlugin plugin) {
        pl = plugin;
    }


    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null)
            return;

        Player killer = e.getEntity().getKiller();
        Player player = e.getEntity().getPlayer();


        CPlayer pk = pl.players.get(killer);
        CPlayer pp = pl.players.get(player);
        if (pk.compMode() && pp.compMode()) {
            pk.addKills(1);
            pp.addDeaths(1);
        }
        if (killer != null)
            rewardKiller(killer, player);

    }

    public void rewardKiller(Player player, Player dead) {
        double amount = pl.config.getDouble("kill-rewards.reward-amount");
        EconomyResponse r = KitPvPPlugin.econ.depositPlayer(player, amount);
        if (r.transactionSuccess()) {
            String msg = pl.config.getString("kill-rewards.message");
            msg = msg.replaceAll("%m", Double.toString(amount));
            msg = msg.replaceAll("%p", dead.getName());
            player.sendMessage(msg);
            player.setHealth(20.0);
            player.setFoodLevel(20);
        }
        for (Object s : pl.config.getList("kill-rewards.potion-effects")) {
            for (PotionEffect p : player.getActivePotionEffects()) {
                if (p.getType().toString().toLowerCase().contains(s.toString().toLowerCase())) {
                    player.removePotionEffect(p.getType());
                    continue;
                } else {
                    continue;
                }
            }
        }
    }

}

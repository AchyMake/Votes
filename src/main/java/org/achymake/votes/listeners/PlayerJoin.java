package org.achymake.votes.listeners;

import org.achymake.votes.UpdateChecker;
import org.achymake.votes.Votes;
import org.achymake.votes.data.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    private Database getDatabase() {
        return getInstance().getDatabase();
    }
    private UpdateChecker getUpdateChecker() {
        return getInstance().getUpdateChecker();
    }
    public PlayerJoin() {
        getInstance().getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        getUpdateChecker().getUpdate(player);
        getDatabase().create(player);
    }
}
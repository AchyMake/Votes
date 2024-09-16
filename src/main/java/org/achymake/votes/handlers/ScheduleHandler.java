package org.achymake.votes.handlers;

import org.achymake.votes.Votes;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleHandler {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    private BukkitScheduler getScheduler() {
        return getInstance().getServer().getScheduler();
    }
    public BukkitTask runLater(Runnable runnable, long timer) {
        return getScheduler().runTaskLater(getInstance(), runnable, timer);
    }
    public BukkitTask runAsynchronously(Runnable runnable) {
        return getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }
    public boolean isQueued(int taskID) {
        return getScheduler().isQueued(taskID);
    }
    public void cancel(int taskID) {
        getScheduler().cancelTask(taskID);
    }
    public void cancelAll() {
        getScheduler().cancelTasks(getInstance());
    }
}
package org.achymake.votes;

import org.achymake.votes.commands.VoteCommand;
import org.achymake.votes.commands.VotesCommand;
import org.achymake.votes.data.Database;
import org.achymake.votes.data.Message;
import org.achymake.votes.handlers.MaterialHandler;
import org.achymake.votes.handlers.ScheduleHandler;
import org.achymake.votes.listeners.PlayerJoin;
import org.achymake.votes.listeners.Vote;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class Votes extends JavaPlugin {
    private static Votes instance;
    private static Database database;
    private static Message message;
    private static ScheduleHandler scheduleHandler;
    private static MaterialHandler materialHandler;
    private static UpdateChecker updateChecker;
    @Override
    public void onEnable() {
        instance = this;
        message = new Message();
        database = new Database();
        scheduleHandler = new ScheduleHandler();
        materialHandler = new MaterialHandler();
        updateChecker = new UpdateChecker();
        commands();
        events();
        reload();
        getMessage().sendLog(Level.INFO, "Enabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        getScheduleHandler().cancelAll();
    }
    private void commands() {
        new VoteCommand();
        new VotesCommand();
    }
    private void events() {
        new PlayerJoin();
        new Vote();
    }
    public void reload() {
        var file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);
            try {
                getConfig().save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
        getDatabase().reload();
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public MaterialHandler getMaterialHandler() {
        return materialHandler;
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public PluginManager getManager() {
        return getServer().getPluginManager();
    }
    public Message getMessage() {
        return message;
    }
    public Database getDatabase() {
        return database;
    }
    public static Votes getInstance() {
        return instance;
    }
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
    public String getMinecraftVersion() {
        return getServer().getBukkitVersion();
    }
    public String getMinecraftProvider() {
        return getServer().getName();
    }
    public boolean isSpigot() {
        return getMinecraftProvider().equals("CraftBukkit");
    }
}
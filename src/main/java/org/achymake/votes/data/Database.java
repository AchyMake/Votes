package org.achymake.votes.data;

import org.achymake.votes.Votes;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class Database {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private String getUUID(OfflinePlayer offlinePlayer) {
        return offlinePlayer.getUniqueId().toString();
    }
    private OfflinePlayer getOfflinePlayer(String uuidString) {
        return getInstance().getServer().getOfflinePlayer(UUID.fromString(uuidString));
    }
    private final File file = new File(getInstance().getDataFolder(), "database.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public boolean exists(OfflinePlayer offlinePlayer) {
        return config.getKeys(false).contains(getUUID(offlinePlayer));
    }
    public void create(OfflinePlayer offlinePlayer) {
        if (!exists(offlinePlayer)) {
            config.set(getUUID(offlinePlayer), 0);
            save();
        }
    }
    public List<OfflinePlayer> getOfflinePlayers() {
        var offlinePlayers = new ArrayList<OfflinePlayer>();
        config.getKeys(false).forEach(uuid -> offlinePlayers.add(getOfflinePlayer(uuid)));
        return offlinePlayers;
    }
    public void addVote(OfflinePlayer offlinePlayer) {
        config.set(getUUID(offlinePlayer), getVoted(offlinePlayer) + 1);
        save();
    }
    public int getVoted(OfflinePlayer offlinePlayer) {
        return config.getInt(getUUID(offlinePlayer));
    }
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            config.options().copyDefaults(true);
            save();
        }
    }
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public FileConfiguration getConfig() {
        return config;
    }
}
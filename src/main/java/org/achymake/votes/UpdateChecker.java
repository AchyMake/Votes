package org.achymake.votes;

import org.achymake.votes.data.Message;
import org.achymake.votes.handlers.ScheduleHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UpdateChecker {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private ScheduleHandler getSchedule() {
        return getInstance().getScheduleHandler();
    }
    private String getName() {
        return getInstance().name();
    }
    private String getVersion() {
        return getInstance().version();
    }
    private boolean notifyUpdate() {
        return getConfig().getBoolean("notify-update");
    }
    public void getUpdate(Player player) {
        if (notifyUpdate()) {
            if (player.hasPermission("votes.event.join.update")) {
                getSchedule().runLater(new Runnable() {
                    @Override
                    public void run() {
                        getLatest((latest) -> {
                            if (!getVersion().equals(latest)) {
                                getMessage().send(player, getName() + "&6 has new update:");
                                getMessage().send(player, "-&a https://www.spigotmc.org/resources/119633/");
                            }
                        });
                    }
                }, 5);
            }
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            getSchedule().runAsynchronously(new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        if (!getVersion().equals(latest)) {
                            getMessage().sendLog(Level.INFO, getName() + " has new update:");
                            getMessage().sendLog(Level.INFO, "- https://www.spigotmc.org/resources/119633/");
                        }
                    });
                }
            });
        }
    }
    public void getLatest(Consumer<String> consumer) {
        try (var inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 119633).openStream()) {
            var scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            } else {
                inputStream.close();
            }
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
}
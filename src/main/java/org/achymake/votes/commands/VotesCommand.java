package org.achymake.votes.commands;

import org.achymake.votes.Votes;
import org.achymake.votes.data.Database;
import org.achymake.votes.data.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class VotesCommand implements CommandExecutor, TabCompleter {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    private Database getDatabase() {
        return getInstance().getDatabase();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public VotesCommand() {
        getInstance().getCommand("votes").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&6You have voted&f " + getDatabase().getVotedFormatted(player) + "&6 times");
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("votes.command.votes.top")) {
                        getMessage().send(player, "&6Top 10 Voters:");
                        List<Map.Entry<OfflinePlayer, Integer>> list = new ArrayList<>(getVoters());
                        for (int i = 0; i < list.size(); i++) {
                            Map.Entry<OfflinePlayer, Integer> test = list.get(i);
                            OfflinePlayer offlinePlayer = test.getKey();
                            int voted = test.getValue();
                            int placed = i + 1;
                            getMessage().send(player, "&6" + placed + "&f " + offlinePlayer.getName() + "&a " + getDatabase().format(voted));
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission("votes.command.votes.reload")) {
                            getInstance().reload();
                            getMessage().send(player, "&6Votes:&f reloaded");
                            return true;
                        }
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    getMessage().send(consoleCommandSender, "&6Top 10 Voters:");
                    List<Map.Entry<OfflinePlayer, Integer>> list = new ArrayList<>(getVoters());
                    for (int i = 0; i < list.size(); i++) {
                        Map.Entry<OfflinePlayer, Integer> test = list.get(i);
                        OfflinePlayer offlinePlayer = test.getKey();
                        int voted = test.getValue();
                        int placed = i + 1;
                        getMessage().send(consoleCommandSender, "&6" + placed + "&f " + offlinePlayer.getName() + "&a " + getDatabase().format(voted));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    getInstance().reload();
                    getMessage().send(consoleCommandSender, "Votes: reloaded");
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("votes.command.votes.top")) {
                    commands.add("top");
                }
                if (player.hasPermission("votes.command.votes.reload")) {
                    commands.add("reload");
                }
            }
        }
        return commands;
    }
    private Set<Map.Entry<OfflinePlayer, Integer>> getVoters() {
        Map<OfflinePlayer, Integer> playerVotes = new HashMap<>();
        getDatabase().getOfflinePlayers().forEach(offlinePlayer -> playerVotes.put(offlinePlayer, getDatabase().getVoted(offlinePlayer)));
        List<Map.Entry<OfflinePlayer, Integer>> list = new ArrayList<>(playerVotes.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        Map<OfflinePlayer, Integer> result = new LinkedHashMap<>();
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (Map.Entry<OfflinePlayer, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet();
    }
}
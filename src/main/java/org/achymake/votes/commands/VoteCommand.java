package org.achymake.votes.commands;

import org.achymake.votes.Votes;
import org.achymake.votes.data.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoteCommand implements CommandExecutor, TabCompleter {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public VoteCommand() {
        getInstance().getCommand("vote").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().sendStringList(player, getInstance().getConfig().getStringList("vote"));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
        }
        return commands;
    }
}
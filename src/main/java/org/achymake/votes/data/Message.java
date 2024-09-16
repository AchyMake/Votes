package org.achymake.votes.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.achymake.votes.Votes;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public class Message {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    public void send(Player player, String message) {
        player.sendMessage(addColor(message));
    }
    public void sendStringList(Player player, List<String> strings) {
        strings.forEach(string -> send(player, string.replaceAll("%player%", player.getName())));
    }
    public void sendAll(String message) {
        getInstance().getServer().getOnlinePlayers().forEach(player -> send(player, message));
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void send(ConsoleCommandSender sender, String message) {
        sender.sendMessage(message);
    }
    public void sendLog(Level level, String message) {
        getInstance().getLogger().log(level, message);
    }
}
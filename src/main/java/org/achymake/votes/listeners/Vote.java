package org.achymake.votes.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import org.achymake.votes.Votes;
import org.achymake.votes.data.Database;
import org.achymake.votes.data.Message;
import org.achymake.votes.handlers.MaterialHandler;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Vote implements Listener {
    private Votes getInstance() {
        return Votes.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Database getDatabase() {
        return getInstance().getDatabase();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public Vote() {
        getInstance().getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onVote(VotifierEvent event) {
        var offlinePlayer = getInstance().getServer().getOfflinePlayer(event.getVote().getUsername());
        var service = event.getVote().getServiceName();
        var player = getInstance().getServer().getPlayer(offlinePlayer.getUniqueId());
        if (player != null) {
            getMaterialHandler().giveItems(player, getItems());
        }
        dispatchCommands(offlinePlayer);
        if (!service.equalsIgnoreCase("testvote")) {
            getDatabase().addVote(offlinePlayer);
        }
        if (getConfig().getBoolean("broadcast.enable")) {
            var addPlayer = getConfig().getString("broadcast.message").replaceAll("%player%", offlinePlayer.getName());
            var result = addPlayer.replaceAll("%service%", service);
            getMessage().sendAll(result);
        }
    }
    public void dispatchCommands(OfflinePlayer offlinePlayer) {
        if (getConfig().getStringList("reward.commands").isEmpty())return;
        for (String commands : getConfig().getStringList("reward.commands")) {
            String result = commands.replaceAll("%player%", offlinePlayer.getName());
            getInstance().getServer().dispatchCommand(getInstance().getServer().getConsoleSender(), result);
        }
    }
    private ConfigurationSection getSection(String path) {
        return getConfig().getConfigurationSection(path);
    }
    public List<ItemStack> getItems() {
        List<ItemStack> itemStacks = new ArrayList<>();
        FileConfiguration config = getConfig();
        for (String itemSections : getSection("reward.items").getKeys(false)) {
            String itemSection = "reward.items." + itemSections;
            String itemType = config.getString(itemSection + ".type");
            int itemAmount = config.getInt(itemSection + ".amount");
            ItemStack itemStack = new ItemStack(Material.valueOf(itemType), itemAmount);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (config.isString(itemSection + ".name")) {
                String name = getMessage().addColor(config.getString(itemSection + ".name"));
                itemMeta.setDisplayName(name);
            }
            if (config.isList(itemSection + ".lore")) {
                List<String> lore = new ArrayList<>();
                for (String lores : config.getStringList(itemSection + ".lore")) {
                    lore.add(getMessage().addColor(lores));
                }
                itemMeta.setLore(lore);
            }
            if (config.isConfigurationSection(itemSection + ".enchantments")) {
                for (String enchantmentSections : getSection(itemSection + ".enchantments").getKeys(false)) {
                    String enchantmentSection = itemSection + ".enchantments." + enchantmentSections;
                    String enchantType = config.getString(enchantmentSection + ".type");
                    int lvl = config.getInt(enchantmentSection + ".amount");
                    itemMeta.addEnchant(Enchantment.getByName(enchantType), lvl, true);
                }
            }
            itemStack.setItemMeta(itemMeta);
            itemStacks.add(itemStack);
        }
        return itemStacks;
    }
}
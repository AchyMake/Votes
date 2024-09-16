package org.achymake.votes.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class MaterialHandler {
    public void giveItems(Player player, Collection<ItemStack> itemStacks) {
        for (var itemStack : itemStacks) {
            if (Arrays.asList(player.getInventory().getStorageContents()).contains(null)) {
                player.getInventory().addItem(itemStack);
            } else {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
        }
    }
}
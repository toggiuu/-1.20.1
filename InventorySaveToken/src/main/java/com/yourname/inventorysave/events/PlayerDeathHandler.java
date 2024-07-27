package com.yourname.inventorysave.events;

import com.yourname.inventorysave.items.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod.EventBusSubscriber
public class PlayerDeathHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            boolean hasToken = hasInventorySaveToken(player);
            
            if (!hasToken) {
                dropAllItems(player);
                player.sendSystemMessage(Component.literal("You didn't have an Inventory Save Token. Your items have been dropped."));
                LOGGER.info("Player {} died without Inventory Save Token. Items dropped.", player.getName().getString());
            } else {
                player.sendSystemMessage(Component.literal("Your inventory has been saved!"));
                LOGGER.info("Player {} died with Inventory Save Token. Inventory saved.", player.getName().getString());
            }
        }
    }

    private static boolean hasInventorySaveToken(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == ModItems.INVENTORY_SAVE_TOKEN.get()) {
                return true;
            }
        }
        return false;
    }

    private static void removeInventorySaveToken(Player player) {
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.getItem() == ModItems.INVENTORY_SAVE_TOKEN.get()) {
                player.getInventory().removeItem(i, 1);
                break;
            }
        }
    }
    private static void dropAllItems(Player player) {
        List<ItemStack> inventory = player.getInventory().items;
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty() && stack.getItem() != ModItems.INVENTORY_SAVE_TOKEN.get()) {
                ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack.copy());
                player.level().addFreshEntity(itemEntity);
            }
        }
        player.getInventory().clearContent();
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        boolean hadToken = hasInventorySaveToken(player);
        
        if (hadToken) {
            removeInventorySaveToken(player);
            player.sendSystemMessage(Component.literal("Your inventory has been restored!"));
            LOGGER.info("Player {} respawned with saved inventory.", player.getName().getString());
        } else {
            
            player.sendSystemMessage(Component.literal("Your inventory was not saved."));
            LOGGER.info("Player {} respawned without saved inventory.", player.getName().getString());
        }
    }
}
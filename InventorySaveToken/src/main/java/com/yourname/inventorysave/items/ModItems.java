package com.yourname.inventorysave.items;

import com.yourname.inventorysave.InventorySaveMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, InventorySaveMod.MODID);

    public static final RegistryObject<Item> INVENTORY_SAVE_TOKEN = ITEMS.register("inventory_save_token",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
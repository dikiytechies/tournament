package com.dikiytechies.tournament.item;

import com.dikiytechies.tournament.AddonMain;
import com.dikiytechies.tournament.init.AddonBlocks;
import com.github.standobyte.jojo.init.ModItems;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AddonItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AddonMain.MOD_ID);

    public static final RegistryObject<BlockItem> SPIKES = ITEMS.register("spikes",
            () -> new BlockItem(AddonBlocks.SPIKES.get(), new Item.Properties().tab(ModItems.MAIN_TAB)));
}

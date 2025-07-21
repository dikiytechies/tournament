package com.dikiytechies.tournament.item;

import com.dikiytechies.tournament.AddonMain;
import com.dikiytechies.tournament.init.AddonBlocks;
import com.dikiytechies.tournament.init.AddonSounds;
import com.github.standobyte.jojo.init.ModItems;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AddonItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AddonMain.MOD_ID);

    public static final RegistryObject<BlockItem> SPIKES = ITEMS.register("spikes",
            () -> new BlockItem(AddonBlocks.SPIKES.get(), new Item.Properties().tab(ModItems.MAIN_TAB)));
    public static final RegistryObject<BlockItem> TILE = ITEMS.register("tile",
            () -> new BlockItem(AddonBlocks.TILE.get(), new Item.Properties().tab(ModItems.MAIN_TAB)));
    public static final RegistryObject<BlockItem> LIGHT_BULB = ITEMS.register("light_bulb",
            () -> new BlockItem(AddonBlocks.LIGHT_BULB.get(), new Item.Properties().tab(ModItems.MAIN_TAB)));
    public static final RegistryObject<BlockItem> FAKE_SPIKES = ITEMS.register("fake_spikes",
            () -> new BlockItem(AddonBlocks.FAKE_SPIKES.get(), new Item.Properties().tab(ModItems.MAIN_TAB)));
    public static final RegistryObject<Item> ASSGORE_RECORD = ITEMS.register("assgore_disc",
            () -> new MusicDiscItem(2, AddonSounds.ASSGORE_RECORD, new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
}

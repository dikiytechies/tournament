package com.dikiytechies.tournament;

import com.dikiytechies.tournament.init.AddonBlocks;
import com.dikiytechies.tournament.item.AddonItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Your addon's main file

@Mod(AddonMain.MOD_ID)
public class AddonMain {
    public static final String MOD_ID = "tournament";
    public static final Logger LOGGER = LogManager.getLogger();

    public AddonMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        initVanillaRegistries(modEventBus);
    }
    private void initVanillaRegistries(IEventBus modEventBus) {
        AddonBlocks.BLOCKS.register(modEventBus);
        AddonItems.ITEMS.register(modEventBus);
    }
}

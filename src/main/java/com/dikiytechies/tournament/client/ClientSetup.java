package com.dikiytechies.tournament.client;

import com.dikiytechies.tournament.AddonMain;
import com.dikiytechies.tournament.init.AddonBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(AddonBlocks.SPIKES.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(AddonBlocks.FAKE_SPIKES.get(), RenderType.cutoutMipped());
        });
    };
}

package com.dikiytechies.tournament.init;

import com.dikiytechies.tournament.AddonMain;
import com.github.standobyte.jojo.JojoMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AddonSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AddonMain.MOD_ID);

    public static final RegistryObject<SoundEvent> ASSGORE_RECORD = register("assgore_record");

    private static RegistryObject<SoundEvent> register(String regPath) {
        return SOUNDS.register(regPath, () -> new SoundEvent(new ResourceLocation(AddonMain.MOD_ID, regPath)));
    }
}

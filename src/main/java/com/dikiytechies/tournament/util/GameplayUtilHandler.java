package com.dikiytechies.tournament.util;

import com.dikiytechies.tournament.AddonMain;
import com.dikiytechies.tournament.init.AddonBlocks;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.potion.BleedingEffect;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.OptionalInt;

import static com.github.standobyte.jojo.potion.BleedingEffect.splashBlood;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class GameplayUtilHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingEntitySpikesFall(LivingFallEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (!living.level.isClientSide()) {
            if (living.level.getBlockState(living.blockPosition().below()).getBlock() == AddonBlocks.SPIKES.get()) {
                LazyOptional<INonStandPower> powerOptional = INonStandPower.getNonStandPowerOptional(living);
                if (powerOptional.isPresent()) {
                    if (powerOptional.map(power ->
                        power.getType() == ModPowers.VAMPIRISM.get() || power.getType() == ModPowers.ZOMBIE.get()).orElse(false)) {
                        System.out.println(event.getDistance() + " " + powerOptional.map(power -> 2.5f * power.getType().getLeapStrength(powerOptional.resolve().get())).orElse(Float.MAX_VALUE));
                        if (event.getDistance() == 0.0f || event.getDistance() > powerOptional.map(power -> 2.5f * power.getType().getLeapStrength(powerOptional.resolve().get())).orElse(Float.MAX_VALUE)) {
                            float distance = event.getDistance() == 0? 4: MathHelper.clamp(event.getDistance(), 0, 15);
                            if (distance > 3) {
                                powerOptional.resolve().get().consumeEnergy(distance * 2.5f);
                                Vector3d particlesPos = living.getBoundingBox().getCenter();
                                splashBlood(living.level, particlesPos, distance, distance,
                                        OptionalInt.of((int) distance / 2), Optional.of(living));
                                living.level.playSound(null, living.blockPosition(), ModSounds.STONE_MASK_ACTIVATION.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
                            }
                            return;
                        }
                    }
                }
                event.setDamageMultiplier(event.getDamageMultiplier() * 2f);
            }
        }
    }
}

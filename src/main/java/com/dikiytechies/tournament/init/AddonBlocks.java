package com.dikiytechies.tournament.init;

import com.dikiytechies.tournament.AddonMain;
import com.dikiytechies.tournament.block.SpikesMultiBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AddonBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AddonMain.MOD_ID);

    public static final RegistryObject<SpikesMultiBlock> SPIKES = BLOCKS.register("spikes",
            () -> new SpikesMultiBlock(AbstractBlock.Properties.of(Material.METAL).harvestLevel(2).requiresCorrectToolForDrops()
                    .harvestTool(ToolType.PICKAXE).strength(6f)));
}

package com.dikiytechies.tournament.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class FakeSpikesMultiBlock extends SpikesMultiBlock {
    public FakeSpikesMultiBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity) {}

    public void fallOn(World world, BlockPos blockPos, Entity entity, float distance) {
        entity.causeFallDamage(distance, 0.0F);
    }
    public void updateEntityAfterFallOn(IBlockReader blockReader, Entity entity) {
        this.bounceUp(entity);
    }
    private void bounceUp(Entity entity) {
        Vector3d vector3d = entity.getDeltaMovement();
        if (vector3d.y < 0.0D) {
            double d0 = entity instanceof LivingEntity ? 1.2D : 1.0D;
            entity.setDeltaMovement(vector3d.x, -vector3d.y * d0, vector3d.z);
        }

    }
}

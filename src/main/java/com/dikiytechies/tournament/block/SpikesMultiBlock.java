package com.dikiytechies.tournament.block;

import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.potion.BleedingEffect;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonUtil;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SpikesMultiBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape BOTTOM_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape TOP_SHAPE = Block.box(4.5D, 0.0D, 4.5D, 11.5D, 14.0D, 11.5D);
    protected static final VoxelShape TOP_SHAPE_FOR_UNDEADS = Block.box(4.5D, 0.0D, 4.5D, 11.5D, 10.0D, 11.5D);
    public SpikesMultiBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public void stepOn(World p_176199_1_, BlockPos p_176199_2_, Entity p_176199_3_) {
        super.stepOn(p_176199_1_, p_176199_2_, p_176199_3_);
    }

    @Override
    public void entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        if (!entity.isInvulnerableTo(DamageSource.CACTUS)) {
            if (entity instanceof LivingEntity && entity.isAlive()) {
                LivingEntity living = (LivingEntity) entity;
                if (HamonUtil.preventBlockDamage(living, world, blockPos, blockState, DamageSource.CACTUS, 1.0f)) return;
                if (JojoModUtil.isUndeadOrVampiric(living)) return;
                if (JojoModUtil.canBleed(living)) {
                    if ((living instanceof PlayerEntity) && ((PlayerEntity) living).abilities.instabuild) return;
                    if (living.hurtTime == 0) {
                        int currentAmp = living.hasEffect(ModStatusEffects.BLEEDING.get()) ? living.getEffect(ModStatusEffects.BLEEDING.get()).getAmplifier() : -1;
                        int amp = BleedingEffect.limitAmplifier(living, (int) Math.floor((BleedingEffect.getMaxHealthWithoutBleeding(living) - living.getHealth()) / 4) - 1);
                        if (currentAmp != amp && amp != -1) {
                            living.addEffect(new EffectInstance(ModStatusEffects.BLEEDING.get(), 150, amp, false, false, true));
                            world.playSound(null, living.blockPosition(), ModSounds.STONE_MASK_ACTIVATION.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
                            living.invulnerableTime = 20;
                            return;
                        }
                    }
                }
            }
            entity.hurt(DamageSource.CACTUS, 1f);
        }
    }

    @Override
    public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
        if (entity instanceof LivingEntity) {
            if (JojoModUtil.isUndeadOrVampiric((LivingEntity) entity)) return SoundType.SNOW; //todo replace with stepSoundMixin
        }
        return super.getSoundType(state, world, pos, entity);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader reader, BlockPos blockPos, ISelectionContext selectionContext) {
        return blockState.getValue(HALF) == DoubleBlockHalf.LOWER? BOTTOM_SHAPE: TOP_SHAPE;
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, IWorld world, BlockPos blockPos, BlockPos blockPos1) {
        DoubleBlockHalf doubleblockhalf = blockState.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || blockState1.is(this) && blockState1.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, world, blockPos, blockPos1);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, IBlockReader reader, BlockPos blockPos, ISelectionContext selectionContext) {
        if (!this.hasCollision) return VoxelShapes.empty();
        Entity entity = selectionContext.getEntity();
        if (entity instanceof LivingEntity && JojoModUtil.isUndeadOrVampiric((LivingEntity) entity) && blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return TOP_SHAPE_FOR_UNDEADS;
        } else return getShape(blockState, reader, blockPos, selectionContext);
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        return blockpos.getY() < 255 && ctx.getLevel().getBlockState(blockpos.above()).canBeReplaced(ctx) ? super.getStateForPlacement(ctx) : null;
    }
    @Override
    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        world.setBlock(blockPos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }
    @Override
    public void playerWillDestroy(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        if (!world.isClientSide) {
            if (playerEntity.isCreative()) {
                preventCreativeDropFromBottomPart(world, blockPos, blockState, playerEntity);
            } else {
                dropResources(blockState, world, blockPos, (TileEntity)null, playerEntity, playerEntity.getMainHandItem());
            }
        }

        super.playerWillDestroy(world, blockPos, blockState, playerEntity);
    }
    @Override
    public void playerDestroy(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, @Nullable TileEntity tileEntity, ItemStack itemStack) {
        super.playerDestroy(world, playerEntity, blockPos, Blocks.AIR.defaultBlockState(), tileEntity, itemStack);
    }
    protected static void preventCreativeDropFromBottomPart(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        DoubleBlockHalf doubleblockhalf = blockState.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = blockPos.below();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.getBlock() == blockState.getBlock() && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                world.levelEvent(playerEntity, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> state) {
        state.add(HALF);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, PathType pathType) {
        return false;
    }
}

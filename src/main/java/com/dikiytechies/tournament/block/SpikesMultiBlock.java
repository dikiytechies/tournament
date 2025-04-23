package com.dikiytechies.tournament.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SpikesMultiBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape BOTTOM_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape TOP_SHAPE = Block.box(4.5D, 0.0D, 4.5D, 11.5D, 14.0D, 11.5D);
    public SpikesMultiBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

//    @Override
//    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos blockPos) {
//        return state.getFluidState().isEmpty();
//    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos blockPos, ISelectionContext selectionContext) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER? BOTTOM_SHAPE: TOP_SHAPE;
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, IWorld world, BlockPos blockPos, BlockPos blockPos1) {
        DoubleBlockHalf doubleblockhalf = blockState.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || blockState1.is(this) && blockState1.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(world, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, world, blockPos, blockPos1);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
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
}

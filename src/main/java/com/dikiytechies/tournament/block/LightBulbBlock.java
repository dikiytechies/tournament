package com.dikiytechies.tournament.block;

import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class LightBulbBlock extends HorizontalFaceBlock {
    public static final DirectionProperty HORIZONTAL_FACING = HorizontalBlock.FACING;
    public static final VoxelShape CEILING_SHAPE = Block.box(6.0, 15.0, 6.0, 10.0, 16.0, 10.0);
    public static final VoxelShape WALL_NORTH_SHAPE = Block.box(6.0, 6.0, 15.0, 10.0, 10.0, 16.0);
    public static final VoxelShape WALL_SOUTH_SHAPE = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 1.0);
    public static final VoxelShape WALL_WEST_SHAPE = Block.box(15.0, 6.0, 6.0, 16.0, 10.0, 10.0);
    public static final VoxelShape WALL_EAST_SHAPE = Block.box(0.0, 6.0, 6.0, 1.0, 10.0, 10.0);
    public static final VoxelShape FLOOR_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 1.0, 10.0);
    public LightBulbBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.CEILING));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch(state.getValue(FACE)) {
            case FLOOR:
                return FLOOR_SHAPE;
            case WALL:
                switch(state.getValue(HORIZONTAL_FACING)) {
                    case EAST:
                        return WALL_EAST_SHAPE;
                    case WEST:
                        return WALL_WEST_SHAPE;
                    case SOUTH:
                        return WALL_SOUTH_SHAPE;
                    case NORTH:
                    default:
                        return WALL_NORTH_SHAPE;
                }
            case CEILING:
            default:
                return CEILING_SHAPE;
        }
    }
    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        BlockState blockState = super.updateShape(state, facing, facingState, world, currentPos, facingPos);
        if (blockState == Blocks.AIR.defaultBlockState() && !world.isClientSide() && world instanceof World) {
            Block.popResource((World) world, currentPos, new ItemStack(this.getBlock()));
        }
        return blockState;
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACE, HORIZONTAL_FACING);
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }
    @Override
    public boolean isPathfindable(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, PathType pathType) {
        return true;
    }
}

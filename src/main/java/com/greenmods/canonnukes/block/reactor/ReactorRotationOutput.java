package com.greenmods.canonnukes.block.reactor;

import com.greenmods.canonnukes.block.entity.ModBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReactorRotationOutput extends DirectionalKineticBlock implements IWrenchable, IBE<ReactorRotationOutputBlockEntity> {

    public static final IntegerProperty DIR = IntegerProperty.create("dir", 0, 2);
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");

    public ReactorRotationOutput(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(DIR, 0)
                .setValue(ASSEMBLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DIR, ASSEMBLED);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        // Проверяем, собран ли мультиблок (флаг ASSEMBLED)
        if (state.getValue(ASSEMBLED)) {
            int currentDir = state.getValue(DIR);
            int newDir = (currentDir + 1) % 3;
            level.setBlock(pos, state.setValue(DIR, newDir), 3);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<ReactorRotationOutputBlockEntity> getBlockEntityClass() {
        return ReactorRotationOutputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorRotationOutputBlockEntity> getBlockEntityType() {
        return ModBlockEntities.REACTOR_ROTATION_OUT.get();
    }
}
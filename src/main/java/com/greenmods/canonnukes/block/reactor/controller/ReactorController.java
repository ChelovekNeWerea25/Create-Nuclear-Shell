package com.greenmods.canonnukes.block.reactor.controller;

import com.greenmods.canonnukes.block.ModBlock;
import com.greenmods.canonnukes.block.entity.ModBlockEntities;
import com.greenmods.canonnukes.block.reactor.ReactorBlock;
import com.greenmods.canonnukes.block.reactor.ReactorRotationOutputBlockEntity;
import com.greenmods.canonnukes.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ReactorController extends ReactorBlock implements EntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Map<Vec3i, Supplier<Block>> MULTIBLOCK_STRUCTURE = new HashMap<>();
    public static final int MAX_HEAT = 255;

    static {
        // Базовые блоки (относительно контроллера)
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, 0, 0), () -> ModBlock.REACTOR_CONTOLLER.get());

        //1слой
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, -1, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, -1, -1), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, -1, -2), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, -1, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, -1, -1), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, -1, -2), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, -1, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, -1, -1), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, -1, -2), () -> ModBlock.STEEL_BLOCK.get());
        //2слой
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, 0, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, 0, -1), () -> ModBlock.REACTOR_INPUT.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, 0, -2), () -> ModBlock.STEEL_BLOCK.get());
        //MULTIBLOCK_STRUCTURE.put(new Vec3i(0, 0, 0), () -> ModBlock.STEEL_BLOCK.get());онтроллер уже есть
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, 0, -1), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, 0, -2), () -> ModBlock.REACTOR_ROTATION_OUT.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, 0, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, 0, -1), () -> ModBlock.REACTOR_OUT.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, 0, -2), () -> ModBlock.STEEL_BLOCK.get());

        //3 слой
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, 1, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, 1, -1), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(1, 1, -2), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, 1, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, 1, -1), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(0, 1, -2), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, 1, 0), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, 1, -1), () -> ModBlock.STEEL_BLOCK.get());
        MULTIBLOCK_STRUCTURE.put(new Vec3i(-1, 1, -2), () -> ModBlock.STEEL_BLOCK.get());

    }


    public ReactorController(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(ReactorBlock.FACING, context.getHorizontalDirection().getOpposite())
                .setValue(ReactorBlock.ASSEMBLED, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        Direction facing = state.getValue(ReactorBlock.FACING);

        if (heldItem.is(ModItems.NUKE_ICON.get())) {
            if (!level.isClientSide) {
                boolean valid = validateMultiblock(level, pos, facing, player);
                if (valid) {
                    player.displayClientMessage(Component.literal("§aМультиблок успешно собран!"), false);
                    level.setBlockAndUpdate(pos, state.setValue(ReactorBlock.ASSEMBLED, true));
                } else {
                    player.displayClientMessage(Component.literal("§cСтруктура не собрана. Ошибки выше."), false);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }
    private boolean validateMultiblock(Level level, BlockPos controllerPos, Direction facing, Player player) {
        boolean isValid = true;
        Direction right = facing.getClockWise();

        // Проверка структуры и флагов
        for (Map.Entry<Vec3i, Supplier<Block>> entry : MULTIBLOCK_STRUCTURE.entrySet()) {
            Vec3i relPos = entry.getKey();
            Block expectedBlock = entry.getValue().get();

            BlockPos checkPos = controllerPos
                    .relative(facing, relPos.getZ())
                    .relative(right, relPos.getX())
                    .above(relPos.getY());

            BlockState checkState = level.getBlockState(checkPos);
            Block actualBlock = checkState.getBlock();

            // Проверка типа блока
            if (actualBlock != expectedBlock) {
                isValid = false;
                if (player != null) {
                    player.sendSystemMessage(Component.literal(
                            String.format("Ошибка в [%d, %d, %d]: нужен %s, а не %s",
                                    relPos.getX(), relPos.getY(), relPos.getZ(),
                                    expectedBlock.getName().getString(),
                                    actualBlock.getName().getString()
                            )
                    ));
                }
                continue;
            }

            // Проверка флага ASSEMBLED (если блок уже в другом мультиблоке)
            if (checkState.hasProperty(ReactorBlock.ASSEMBLED)) {
                if (checkState.getValue(ReactorBlock.ASSEMBLED)) {
                    isValid = false;
                    if (player != null) {
                        player.sendSystemMessage(Component.literal(
                                String.format("Ошибка: блок [%d, %d, %d] уже в другом мультиблоке!",
                                        relPos.getX(), relPos.getY(), relPos.getZ()
                                )
                        ));
                    }
                }
            }
        }

        // Если структура верна, устанавливаем флаги
        if (isValid) {
            for (Map.Entry<Vec3i, Supplier<Block>> entry : MULTIBLOCK_STRUCTURE.entrySet()) {
                Vec3i relPos = entry.getKey();
                BlockPos blockPos = controllerPos
                        .relative(facing, relPos.getZ())
                        .relative(right, relPos.getX())
                        .above(relPos.getY());

                BlockState currentState = level.getBlockState(blockPos);
                if (currentState.hasProperty(ReactorBlock.ASSEMBLED)) {
                    level.setBlock(blockPos,
                            currentState.setValue(ReactorBlock.ASSEMBLED, true),
                            3 // Флаг для обновления клиента
                    );
                }
            }
        }

        return isValid;
    }

    public void buildMultiblock(Level level, BlockPos controllerPos, Direction facing, Player player) {
        Direction right = facing.getClockWise();

        for (Map.Entry<Vec3i, Supplier<Block>> entry : MULTIBLOCK_STRUCTURE.entrySet()) {
            Vec3i relPos = entry.getKey();
            Block blockToPlace = entry.getValue().get();

            // Пропускаем сам контроллер (его не заменяем)
            if (relPos.equals(new Vec3i(0, 0, 0))) continue;

            BlockPos placePos = controllerPos
                    .relative(facing, relPos.getZ())
                    .relative(right, relPos.getX())
                    .above(relPos.getY());

            if (!level.isClientSide) {
                level.setBlockAndUpdate(placePos, blockToPlace.defaultBlockState());
            }
        }

        if (player != null && !level.isClientSide) {
            player.sendSystemMessage(Component.literal("Мультиблок построен!"));
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorControllerEntity(ModBlockEntities.REACTOR_CONTROLLER.get(), pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type,
                ModBlockEntities.REACTOR_CONTROLLER.get(),
                ReactorControllerEntity::tick
        );
    }

    // Вспомогательный метод для создания тикера
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType,
            BlockEntityType<E> clientType,
            BlockEntityTicker<? super E> ticker
    ) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // Получаем направление, в котором "смотрит" контроллер
            Direction facing = state.getValue(FACING);
            Direction right = facing.getClockWise();
            BlockPos rotationOutPos = getRelativePos(pos, facing, new Vec3i(0, 0, -2));
            BlockEntity be = level.getBlockEntity(rotationOutPos);
            if (be instanceof ReactorRotationOutputBlockEntity output) {
                output.stopRotation();
                // Принудительное обновление состояния
                level.sendBlockUpdated(rotationOutPos, be.getBlockState(), be.getBlockState(), 3);
            }

            // Проходим по структуре и сбрасываем флаг ASSEMBLED
            for (Map.Entry<Vec3i, Supplier<Block>> entry : MULTIBLOCK_STRUCTURE.entrySet()) {
                Vec3i relPos = entry.getKey();
                BlockPos checkPos = pos
                        .relative(facing, relPos.getZ())
                        .relative(right, relPos.getX())
                        .above(relPos.getY());

                BlockState checkState = level.getBlockState(checkPos);

                if (checkState.hasProperty(ReactorBlock.ASSEMBLED) && checkState.getValue(ReactorBlock.ASSEMBLED)) {
                    level.setBlock(checkPos, checkState.setValue(ReactorBlock.ASSEMBLED, false), 3);
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }


    private BlockPos getRelativePos(BlockPos controllerPos, Direction facing, Vec3i offset) {
        Direction right = facing.getClockWise();
        return controllerPos
                .relative(facing, offset.getZ())
                .relative(right, offset.getX())
                .above(offset.getY());
    }

}
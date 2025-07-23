package com.greenmods.canonnukes.block.reactor.controller;

import com.greenmods.canonnukes.block.reactor.ReactorBlock;
import com.greenmods.canonnukes.block.reactor.ReactorInputBlockEntity;
import com.greenmods.canonnukes.block.reactor.ReactorOutputBlockEntity;
import com.greenmods.canonnukes.block.reactor.ReactorRotationOutputBlockEntity;
import com.greenmods.canonnukes.item.ModItems;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ReactorControllerEntity2 extends BlockEntity implements IHaveGoggleInformation {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorControllerEntity2.class);
    private static final int PROCESS_TICKS = 100;
    private static final int MAX_HEAT = 255;
    private static final float HEATING_RATE = 3.0f;
    private static final float COOLING_RATE = 1.5f;

    private int progress;
    private float heat = 0;

    public ReactorControllerEntity2(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ReactorControllerEntity2 entity) {
        if (level.isClientSide) return;

        boolean assembled = state.getValue(ReactorBlock.ASSEMBLED);
        if (!assembled) {
            entity.resetRotationOut(level, pos, state);
            if (entity.progress != 0 || entity.heat != 0) {
                entity.progress = 0;
                entity.heat = 0;
                entity.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
            return;
        }

        if (!validateMultiblockSilent(level, pos, state.getValue(ReactorBlock.FACING))) {
            level.setBlock(pos, state.setValue(ReactorBlock.ASSEMBLED, false), 3);
            entity.resetRotationOut(level, pos, state);
            return;
        }

        Direction facing = state.getValue(ReactorBlock.FACING);
        BlockPos inputPos = getRelativePos(pos, facing, new Vec3i(1, 0, -1));
        BlockPos outputPos = getRelativePos(pos, facing, new Vec3i(-1, 0, -1));
        BlockPos rotationOutPos = getRelativePos(pos, facing, new Vec3i(0, 0, -2));

        BlockEntity inputEntity = level.getBlockEntity(inputPos);
        BlockEntity outputEntity = level.getBlockEntity(outputPos);
        BlockEntity rotationOutEntity = level.getBlockEntity(rotationOutPos);

        if (!(inputEntity instanceof ReactorInputBlockEntity input) ||
                !(outputEntity instanceof ReactorOutputBlockEntity output) ||
                !(rotationOutEntity instanceof ReactorRotationOutputBlockEntity rotationOut)) {
            return;
        }

        processUranium(input, output, entity, level, pos, state);
        if (rotationOutEntity instanceof ReactorRotationOutputBlockEntity rotationOute) {
            rotationOute.setSpeed((int) entity.heat);
        }
    }

    private void resetRotationOut(Level level, BlockPos pos, BlockState state) {
        if (level == null) return;
        Direction facing = state.getValue(ReactorBlock.FACING);
        BlockPos rotationOutPos = getRelativePos(pos, facing, new Vec3i(0, 0, -2));
        BlockEntity be = level.getBlockEntity(rotationOutPos);
        if (be instanceof ReactorRotationOutputBlockEntity rotationOut) {
            rotationOut.setSpeed(0);
        }
    }

    private static boolean validateMultiblockSilent(Level level, BlockPos controllerPos, Direction facing) {
        Direction right = facing.getClockWise();

        for (Map.Entry<Vec3i, Supplier<Block>> entry : ReactorController.MULTIBLOCK_STRUCTURE.entrySet()) {
            Vec3i relPos = entry.getKey();
            Block expectedBlock = entry.getValue().get();

            BlockPos checkPos = controllerPos
                    .relative(facing, relPos.getZ())
                    .relative(right, relPos.getX())
                    .above(relPos.getY());

            BlockState checkState = level.getBlockState(checkPos);
            Block actualBlock = checkState.getBlock();

            if (actualBlock != expectedBlock) {
                return false;
            }

            if (checkState.hasProperty(ReactorBlock.ASSEMBLED) && !checkState.getValue(ReactorBlock.ASSEMBLED)) {
                return false;
            }
        }
        return true;
    }

    private static void processUranium(ReactorInputBlockEntity input, ReactorOutputBlockEntity output,
                                       ReactorControllerEntity2 controller, Level level, BlockPos pos, BlockState state) {
        ItemStack inputStack = input.getItem(0);
        ItemStack outputStack = output.getItem(0);
        boolean hasUranium = !inputStack.isEmpty() && inputStack.is(ModItems.URAN_INGOT.get());
        float oldHeat = controller.heat;

        // Heat management
        if (hasUranium) {
            controller.heat = Math.min(MAX_HEAT, controller.heat + HEATING_RATE);
        } else {
            controller.heat = Math.max(0, controller.heat - COOLING_RATE);
        }

        // Processing logic
        boolean canProcess = hasUranium &&
                (outputStack.isEmpty() ||
                        (outputStack.is(ModItems.PROCESSED_URAN.get()) &&
                                outputStack.getCount() < outputStack.getMaxStackSize()));

        if (canProcess) {
            int oldProgress = controller.progress;
            controller.progress++;

            if (controller.progress >= PROCESS_TICKS) {
                input.removeItem(0, 1);
                if (outputStack.isEmpty()) {
                    output.setItem(0, new ItemStack(ModItems.PROCESSED_URAN.get(), 1));
                } else {
                    outputStack.grow(1);
                }
                controller.progress = 0;
            }

            if (controller.progress != oldProgress || controller.heat != oldHeat) {
                controller.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        } else if (controller.progress != 0) {
            controller.progress = 0;
            controller.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private static BlockPos getRelativePos(BlockPos controllerPos, Direction facing, Vec3i offset) {
        Direction right = facing.getClockWise();
        return controllerPos
                .relative(facing, offset.getZ())
                .relative(right, offset.getX())
                .above(offset.getY());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", progress);
        tag.putFloat("Heat", heat);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("Progress");
        heat = tag.getFloat("Heat");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("Progress", progress);
        tag.putFloat("Heat", heat);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        progress = tag.getInt("Progress");
        heat = tag.getFloat("Heat");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!hasLevel()) return false;

        tooltip.add(Component.literal("Реакторный контроллер").withStyle(ChatFormatting.YELLOW));

        boolean assembled = getBlockState().getValue(ReactorBlock.ASSEMBLED);
        tooltip.add(Component.literal("Структура: " + (assembled ? "Собрана" : "Не собрана"))
                .withStyle(assembled ? ChatFormatting.GREEN : ChatFormatting.RED));

        tooltip.add(Component.literal("Прогресс: " + progress + " / " + PROCESS_TICKS)
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.literal("Нагрев: " + (int)heat + " / " + MAX_HEAT)
                .withStyle(getHeatColor(heat)));

        return true;
    }

    private ChatFormatting getHeatColor(float heat) {
        float ratio = heat / MAX_HEAT;
        if (ratio < 0.3) return ChatFormatting.GREEN;
        if (ratio < 0.7) return ChatFormatting.YELLOW;
        return ChatFormatting.RED;
    }
    public float getHeat() {
        return heat;
    }
}
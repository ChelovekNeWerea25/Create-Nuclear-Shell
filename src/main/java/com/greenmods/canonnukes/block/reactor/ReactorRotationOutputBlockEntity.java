package com.greenmods.canonnukes.block.reactor;

import com.greenmods.canonnukes.block.reactor.controller.ReactorControllerEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ReactorRotationOutputBlockEntity extends GeneratingKineticBlockEntity {

    private int speed; // Текущая скорость вращения
    private float stressCapacity;
    private static final int FIXED_SPEED = 128; // Постоянная скорость вращения
    private static final float FIXED_STRESS = 2048.0f;// Текущая нагрузка (на основе тепла)

    public ReactorRotationOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        // Автоматическая остановка при разборке мультиблока
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            if (state.hasProperty(ReactorRotationOutput.ASSEMBLED) &&
                    !state.getValue(ReactorRotationOutput.ASSEMBLED) &&
                    (speed != 0 || stressCapacity != 0)) {
                stopRotation();
            }
        }
    }

    /**
     * Обновляет состояние блока на основе данных контроллера
     * @param controller Контроллер реактора
     */
    public void updateFromController(ReactorControllerEntity controller) {
        if (controller.getHeat() > 0) {
            speed = FIXED_SPEED;
            stressCapacity = FIXED_STRESS;
        } else {
            speed = 0;
            stressCapacity = 0;
        }

        updateGeneratedRotation();
        notifyUpdate();
        setChanged();
    }


    /**
     * Останавливает вращение (вызывается при разборке мультиблока)
     */
    public void stopRotation() {
        if (speed != 0 || stressCapacity != 0) {
            speed = 0;
            stressCapacity = 0;
            updateGeneratedRotation();
            notifyUpdate();
            setChanged();
        }
    }


    @Override
    public float getGeneratedSpeed() {
        return speed;
    }

    @Override
    public float calculateAddedStressCapacity() {
        return stressCapacity; // Фиксированная нагрузка
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putInt("Speed", speed);
        tag.putFloat("StressCapacity", stressCapacity);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        speed = tag.getInt("Speed");
        stressCapacity = tag.getFloat("StressCapacity");
    }
}
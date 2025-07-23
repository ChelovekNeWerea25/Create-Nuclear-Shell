package com.greenmods.canonnukes.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.greenmods.canonnukes.fluid.CNFluids;

import static com.greenmods.canonnukes.fluid.CNFluids.URANIUM;

public class UraniumEventHandler {

    public static void handleFluidEffect(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.isAlive() && !(entity.isSpectator())) {
            if (entity.tickCount % 20 == 0) return;
            if (entity.isInFluidType(URANIUM.getType())) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
            }
        }

    }
}

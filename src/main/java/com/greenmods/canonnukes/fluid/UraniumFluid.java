package com.greenmods.canonnukes.fluid;

import com.simibubi.create.AllFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Vector3f;

public class UraniumFluid extends AllFluids.TintedFluidType {
    private static final int COLOR = 0x04D647;

    public UraniumFluid(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties, stillTexture, flowingTexture);
    }

    @Override
    protected int getTintColor(FluidStack stack) {
        return NO_TINT;
    }

    @Override
    protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return COLOR;
    }

    @Override
    protected Vector3f getCustomFogColor() {
        return new Vector3f(0.02f, 1.0f, 0.28f);
    }

    @Override
    protected float getFogDistanceModifier() {
        return 1f / 32f;
    }
}

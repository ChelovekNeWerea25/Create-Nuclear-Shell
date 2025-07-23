package com.greenmods.canonnukes.fluid;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import net.minecraft.resources.ResourceLocation;

public class UraniumFluidFactory {
    public static final FluidTypeFactory INSTANCE = (properties, stillTexture, flowingTexture) ->
            new UraniumFluid(properties, stillTexture, flowingTexture);
}

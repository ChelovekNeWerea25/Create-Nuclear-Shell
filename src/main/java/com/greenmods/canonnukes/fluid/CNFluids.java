package com.greenmods.canonnukes.fluid;

import com.greenmods.canonnukes.CanonNukes;
import com.simibubi.create.AllFluids;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class CNFluids {

    public static final FluidEntry<ForgeFlowingFluid.Flowing> URANIUM =
            CanonNukes.REGISTRATE.standardFluid("uranium", UraniumFluidFactory.INSTANCE)
                    .lang("Liquid Uranium")
                    .properties(p -> p
                            .viscosity(4000)
                            .density(3000)
                            .canSwim(false)
                            .canDrown(true)
                            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                    )
                    .fluidProperties(f -> f
                            .levelDecreasePerBlock(1)
                            .tickRate(20)
                            .slopeFindDistance(4)
                            .explosionResistance(100f)
                    )
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .lang("Uranium Bucket")
                    .build()
                    .register();

    public static void register() {}
}

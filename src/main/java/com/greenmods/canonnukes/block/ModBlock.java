package com.greenmods.canonnukes.block;

import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.block.reactor.ReactorCasing;
import com.greenmods.canonnukes.block.reactor.controller.ReactorController;
import com.greenmods.canonnukes.block.reactor.ReactorInput;
import com.greenmods.canonnukes.block.reactor.ReactorOutput;
import com.greenmods.canonnukes.block.reactor.ReactorRotationOutput;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlock {
    private static final CreateRegistrate REGISTRATE = CanonNukes.REGISTRATE;
    public static final BlockEntry<ReactorCasing> STEEL_BLOCK = REGISTRATE.
            block("steel_block", ReactorCasing::new)
            .properties(p -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f, 9.0f)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .friction(0.8f)
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .lang("Steel Block")
            .item()
            .build()
            .register();
    public static final BlockEntry<ReactorController> REACTOR_CONTOLLER = REGISTRATE.
            block("steel_controller", ReactorController::new)
            .properties(p -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f, 9.0f)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .friction(0.8f)
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .lang("Steel Controller")
            .item()
            .build()
            .register();
    public static final BlockEntry<ReactorInput> REACTOR_INPUT = REGISTRATE.
            block("steel_input",ReactorInput::new)
            .properties(p -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f, 9.0f)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .friction(0.8f)
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .lang("Steel Input")
            .item()
            .build()
            .register();
    public static final BlockEntry<ReactorOutput> REACTOR_OUT = REGISTRATE.
            block("steel_out", ReactorOutput::new)
            .properties(p -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f, 9.0f)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .friction(0.8f)
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .lang("Steel Out")
            .item()
            .build()
            .register();
    public static final BlockEntry<ReactorRotationOutput> REACTOR_ROTATION_OUT = REGISTRATE.
            block("steel_rot_output", ReactorRotationOutput::new)
            .properties(p -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f, 9.0f)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .friction(0.8f)
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .lang("Steel Rotation Output")
            .item()
            .build()
            .register();

    //uran
    public static final BlockEntry<ReactorRotationOutput> URAN_ORE = REGISTRATE.
            block("uran_ore", ReactorRotationOutput::new)
            .properties(p -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.5f, 9.0f)
                    .sound(SoundType.STONE)
                    .friction(0.8f)
                    .lightLevel(state->5)
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .lang("Uran Ore")
            .item()
            .build()
            .register();
    public static final BlockEntry<ReactorRotationOutput> DEEPSLATE_URAN_ORE = REGISTRATE.
            block("deepslate_uran_ore", ReactorRotationOutput::new)
            .properties(p -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f, 9.0f)
                    .sound(SoundType.DEEPSLATE)
                    .friction(0.8f)
                    .lightLevel(state->5)
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .lang("Deepslate Uran Ore")
            .item()
            .build()
            .register();

    public static void register() {}
}

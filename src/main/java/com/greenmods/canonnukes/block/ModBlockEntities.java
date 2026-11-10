package com.greenmods.canonnukes.block;

import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.shell.ModShells;

import com.tterrag.registrate.util.entry.BlockEntityEntry;

import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntityRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockVisual;

/**
 * Block entities of the mod.
 *
 * <p>Since 1.21 the shells of Create: Big Cannons need their own block entity, it keeps the fuze
 * of the shell and renders it.</p>
 */
public class ModBlockEntities {

    public static final BlockEntityEntry<FuzedBlockEntity> FUZED_BLOCK = CanonNukes.REGISTRATE
            .blockEntity("fuzed_block", FuzedBlockEntity::new)
            .visual(() -> FuzedBlockVisual::new)
            .renderer(() -> FuzedBlockEntityRenderer::new)
            .validBlocks(ModShells.NUKE_SHELL)
            .register();

    public static void register() {
    }
}

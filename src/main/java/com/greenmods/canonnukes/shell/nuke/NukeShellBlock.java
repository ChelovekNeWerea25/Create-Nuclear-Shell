package com.greenmods.canonnukes.shell.nuke;

import com.greenmods.canonnukes.block.ModBlockEntities;
import com.greenmods.canonnukes.shell.ModShells;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class NukeShellBlock extends SimpleShellBlock<NukeShellProjectile> {

    public NukeShellBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return simpleCodec(NukeShellBlock::new);
    }

    @Override
    public boolean isBaseFuze() {
        return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE
                .getPropertiesOf(this.getAssociatedEntityType())
                .fuze()
                .baseFuze();
    }

    @Override
    public EntityType<? extends NukeShellProjectile> getAssociatedEntityType() {
        return ModShells.NUKE_SHELL_PROJECTILE.get();
    }

    @Override
    public Class<FuzedBlockEntity> getBlockEntityClass() {
        return FuzedBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FuzedBlockEntity> getBlockEntityType() {
        return ModBlockEntities.FUZED_BLOCK.get();
    }

}
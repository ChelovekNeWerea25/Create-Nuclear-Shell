package com.greenmods.canonnukes.shell.nuke;

import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.shell.ModShells;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class NukeShellBlock extends SimpleShellBlock<NukeShellProjectile> {
    public NukeShellBlock(Properties properties) {
        super(properties);
    }
    @Override
    public boolean isBaseFuze() {
        return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
    }

    @Override
    public EntityType<? extends NukeShellProjectile> getAssociatedEntityType() {
        return ModShells.NUKE_SHELL_PROJECTILE.get();
    }
}

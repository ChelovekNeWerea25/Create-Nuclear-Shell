package com.greenmods.canonnukes.shell.nuke;

import com.greenmods.canonnukes.shell.ModShells;
import com.greenmods.canonnukes.world.explosion.NukeExplosion;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonCommonShellProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class NukeShellProjectile extends FuzedBigCannonProjectile {

    public NukeShellProjectile(EntityType<? extends NukeShellProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void detonate(Position position) {
        if (this.level() instanceof ServerLevel serverLevel) {
            float radius = 35F;

            NukeExplosion explosion = new NukeExplosion(
                    serverLevel,
                    this,
                    null,
                    null,
                    position.x(),
                    position.y(),
                    position.z(),
                    radius,
                    true,
                    Explosion.BlockInteraction.DESTROY
            );

            explosion.explode();
            explosion.finalizeExplosion(true);
        }

        this.discard();
    }

    @Override
    public BlockState getRenderedBlockState() {
        return ModShells.NUKE_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
    }

    @Nonnull
    @Override
    protected BigCannonFuzePropertiesComponent getFuzeProperties() {
        return this.getAllProperties().fuze();
    }

    @Nonnull
    @Override
    protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
        return this.getAllProperties().bigCannonProperties();
    }

    @Nonnull
    @Override
    public EntityDamagePropertiesComponent getDamageProperties() {
        return this.getAllProperties().damage();
    }

    @Nonnull
    @Override
    protected BallisticPropertiesComponent getBallisticProperties() {
        return this.getAllProperties().ballistics();
    }

    protected BigCannonCommonShellProperties getAllProperties() {
        return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this);
    }
}
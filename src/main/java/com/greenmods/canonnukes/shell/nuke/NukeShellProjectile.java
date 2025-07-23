package com.greenmods.canonnukes.shell.nuke;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.google.common.base.Predicates;
import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.shell.ModShells;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
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
import java.util.stream.Stream;

public class NukeShellProjectile extends FuzedBigCannonProjectile {
    public NukeShellProjectile(EntityType<? extends NukeShellProjectile> type, Level level) {
        super(type, level);
    }
    @Override
    protected void detonate(Position position) {
        NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(this.level());
        explosion.copyPosition(this);
        explosion.setSize(AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get().floatValue() * this.getAllProperties().explosion().explosivePower() );
        this.level().addFreshEntity(explosion);
        this.discard();

    }

    @Override
    protected void onTickRotate(){
        super.onTickRotate();
        if (level() instanceof ServerLevel server && tickCount % 10 == 0) {
            getNearbySirens(server, 256).forEach(
                    pos -> {
                        if (server.getBlockEntity(pos) instanceof NuclearSirenBlockEntity siren)
                            siren.setNearestNuclearBomb(this);
                    }
            );
        }
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
    private Stream<BlockPos> getNearbySirens(ServerLevel world, int range) {
        PoiManager poiManager = world.getPoiManager();
        return poiManager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.NUCLEAR_SIREN.getKey()), Predicates.alwaysTrue(), this.blockPosition(), range, PoiManager.Occupancy.ANY);
    }
}

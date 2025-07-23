package com.greenmods.canonnukes.shell;

import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.shell.nuke.NukeShellBlock;
import com.greenmods.canonnukes.shell.nuke.NukeShellProjectile;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.datagen.assets.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler;
import rbasamoyai.ritchiesprojectilelib.RPLTags;

import java.util.function.Consumer;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class ModShells {
    private static final CreateRegistrate REGISTRATE = CanonNukes.REGISTRATE;

    public static final BlockEntry<NukeShellBlock> NUKE_SHELL = REGISTRATE
            .block("nuke_shell", NukeShellBlock::new)
            .transform(shell(MapColor.COLOR_RED))
            .transform(axeOrPickaxe())
            .transform(CBCBuilderTransformers.projectile("projectile/nuke_shell"))
            .transform(CBCBuilderTransformers.safeNbt())
            .loot(CBCBuilderTransformers.shellLoot())
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.createnukecannon.nuke_shell"))
            .lang("Nuclear Shell")
            .item(FuzedProjectileBlockItem::new)
            .transform(CBCBuilderTransformers.fuzedProjectileItem("projectile/nuke_shell"))
            .tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
            .build()
            .register();
    public static final EntityEntry<NukeShellProjectile> NUKE_SHELL_PROJECTILE = cannonProjectile("nuke_shell_projectile", NukeShellProjectile::new, "Nuclear Shell", CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE);

    private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> shell(MapColor color) {
        return b -> b.addLayer(() -> RenderType::solid)
                .properties(p -> p.mapColor(color))
                .properties(p -> p.strength(2.0f, 3.0f))
                .properties(p -> p.sound(SoundType.STONE));
    }
    private static <T extends AbstractBigCannonProjectile> EntityEntry<T>
    cannonProjectile(String id, EntityType.EntityFactory<T> factory, PropertiesTypeHandler<EntityType<?>, ?> handler) {
        return REGISTRATE
                .entity(id, factory, MobCategory.MISC)
                .properties(cannonProperties())
                .renderer(() -> BigCannonProjectileRenderer::new)
                .tag(RPLTags.PRECISE_MOTION)
                .onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
                .register();
    }
    private static <T extends AbstractBigCannonProjectile> EntityEntry<T>
    cannonProjectile(String id, EntityType.EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
        return REGISTRATE
                .entity(id, factory, MobCategory.MISC)
                .properties(cannonProperties())
                .renderer(() -> BigCannonProjectileRenderer::new)
                .lang(enUSdiffLang)
                .tag(RPLTags.PRECISE_MOTION)
                .onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
                .register();
    }
    private static <T> NonNullConsumer<T> configure(Consumer<EntityTypeConfigurator> cons) {
        return b -> cons.accept(EntityTypeConfigurator.of(b));
    }
    private static <T> NonNullConsumer<T> cannonProperties() {
        return configure(c -> c.size(0.8f, 0.8f)
                .fireImmune()
                .updateInterval(1)
                .updateVelocity(false)
                .trackingRange(16));
    }
    public static void register() {
    }
}

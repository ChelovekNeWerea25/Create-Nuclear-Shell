package com.greenmods.canonnukes.block.entity;

import com.greenmods.canonnukes.block.ModBlock;
import com.greenmods.canonnukes.block.nuke.NuclearBombRenderer;
import com.greenmods.canonnukes.block.nuke.NuclearExplosionEntity;
import com.greenmods.canonnukes.block.reactor.ReactorInputBlockEntity;
import com.greenmods.canonnukes.block.reactor.ReactorOutputBlockEntity;
import com.greenmods.canonnukes.block.reactor.ReactorRotationOutputBlockEntity;
import com.greenmods.canonnukes.block.reactor.controller.ReactorControllerEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static com.greenmods.canonnukes.CanonNukes.REGISTRATE;

public class ModBlockEntities {
    public static final BlockEntityEntry<ReactorControllerEntity> REACTOR_CONTROLLER = REGISTRATE
            .blockEntity("reactor_controller", ReactorControllerEntity::new)
            .validBlocks(ModBlock.REACTOR_CONTOLLER)
            .register();
    public static final BlockEntityEntry<ReactorInputBlockEntity> REACTOR_INPUT = REGISTRATE
            .blockEntity("reactor_input", ReactorInputBlockEntity::new)
            .validBlocks(ModBlock.REACTOR_INPUT)
            .register();
    public static final BlockEntityEntry<ReactorOutputBlockEntity> REACTOR_OUTPUT = REGISTRATE
            .blockEntity("reactor_out", ReactorOutputBlockEntity::new)
            .validBlocks(ModBlock.REACTOR_OUT)
            .register();
    public static final BlockEntityEntry<ReactorRotationOutputBlockEntity> REACTOR_ROTATION_OUT = REGISTRATE
            .blockEntity("reactor_rot_out", ReactorRotationOutputBlockEntity::new)
            .validBlocks(ModBlock.REACTOR_ROTATION_OUT)
            .register();
    /*public static final EntityEntry<NuclearExplosionEntity> NUCLEAR_EXPLOSION = REGISTRATE
            .entity("nuclear_explosion", NuclearExplosionEntity::new, MobCategory.MISC)
            .properties(b -> b
                    .sized(1.0F, 1.0F)
                    .fireImmune()
                    .setTrackingRange(16)
                    .setUpdateInterval(1)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(()-> NuclearBombRenderer::new)
            .register();*/


    public static void register() {}
}

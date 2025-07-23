// ClientSetup.java
package com.greenmods.canonnukes.client;

import com.greenmods.canonnukes.block.entity.ModBlockEntities;
import com.greenmods.canonnukes.block.nuke.NuclearBombRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = "canonnukes", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::onClientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onClientSetup(FMLClientSetupEvent event) {

    }
}
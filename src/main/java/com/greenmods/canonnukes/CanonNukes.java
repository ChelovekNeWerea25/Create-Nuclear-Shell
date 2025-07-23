package com.greenmods.canonnukes;

import com.greenmods.canonnukes.block.ModBlock;
import com.greenmods.canonnukes.block.entity.ModBlockEntities;

import com.greenmods.canonnukes.event.UraniumEventHandler;
import com.greenmods.canonnukes.fluid.CNFluids;
import com.greenmods.canonnukes.item.ModItems;
import com.greenmods.canonnukes.setup.CNPonders;
import com.greenmods.canonnukes.setup.CNTab;
import com.greenmods.canonnukes.shell.ModShells;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;

import com.tterrag.registrate.Registrate;
import net.createmod.catnip.lang.FontHelper;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

import rbasamoyai.createbigcannons.ModGroup;

import java.util.concurrent.CompletableFuture;

@Mod(CanonNukes.MODID)
public class CanonNukes
{
    public static final String MODID = "canonnukes";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    public CanonNukes(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        CNTab.register(modEventBus);
        ModBlock.register();
        ModBlockEntities.register();
        ModShells.register();
        ModItems.register();
        CNFluids.register();

        modEventBus.addListener(this::addCreative);
        forgeEventBus.addListener(UraniumEventHandler::handleFluidEffect);
        REGISTRATE.registerEventListeners(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Common setup init");

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == ModGroup.MAIN_TAB_KEY)
            event.accept(ModShells.NUKE_SHELL.asItem());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

        LOGGER.info("server starting init");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            PonderIndex.addPlugin( new CNPonders() );
            LOGGER.info("HELLO FROM CLIENT SETUP");
        }
    }

}

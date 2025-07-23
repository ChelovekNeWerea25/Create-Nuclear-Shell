package com.greenmods.canonnukes.setup;

import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.block.ModBlock;
import com.greenmods.canonnukes.block.reactor.ReactorPonder;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderScenes.register;

public class CNPonders implements PonderPlugin {

    @Override
    public String getModId() {
        return CanonNukes.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        register(helper);
    }

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(ModBlock.REACTOR_CONTOLLER, "reactor", ReactorPonder::ponder);

    }

    static ResourceLocation REACTOR = new ResourceLocation(CanonNukes.MODID, "reactor");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.registerTag(REACTOR)
                .item(ModBlock.REACTOR_CONTOLLER.get())
                .addToIndex()
                .register();
    }
}
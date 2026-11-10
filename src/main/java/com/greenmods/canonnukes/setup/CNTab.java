package com.greenmods.canonnukes.setup;

import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.item.ModItems;
import com.greenmods.canonnukes.shell.ModShells;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CNTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CanonNukes.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CANON_NUKES_TAB =
            TABS.register("canonnukes_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.canonnukes"))
                    .icon(() -> new ItemStack(ModItems.NUKE_ICON.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModShells.NUKE_SHELL.asItem());
                        output.accept(ModShells.INCOMPLETE_NUKE_SHELL.asItem());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

    private CNTab() {
    }
}

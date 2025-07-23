package com.greenmods.canonnukes.setup;

import com.greenmods.canonnukes.CanonNukes;
import com.greenmods.canonnukes.block.ModBlock;
import com.greenmods.canonnukes.item.ModItems;
import com.greenmods.canonnukes.shell.ModShells;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CNTab {
    public static final DeferredRegister<CreativeModeTab> tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CanonNukes.MODID);
    public static final RegistryObject<CreativeModeTab> CANON_NUKES_TAB = tabs.register("canonnukes_tab", ()->CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.canon_nukes_tab"))
            .icon( ()-> new ItemStack( ModItems.NUKE_ICON.get() ) )
            .displayItems((itemDisplayParameters, output) ->{
                output.accept(ModShells.NUKE_SHELL.get());
                output.accept(ModBlock.STEEL_BLOCK.get());
            } )
            .build());
    public static void register(IEventBus eventBus) {
        tabs.register(eventBus);
    }
}

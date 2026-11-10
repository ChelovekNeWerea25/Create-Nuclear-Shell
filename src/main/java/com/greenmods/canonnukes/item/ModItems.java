package com.greenmods.canonnukes.item;

import com.greenmods.canonnukes.CanonNukes;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class ModItems {
    private static final CreateRegistrate REGISTRATE = CanonNukes.REGISTRATE;

    /** Icon of the creative tab of this mod, it is not meant to be used in survival. */
    public static final ItemEntry<Item> NUKE_ICON = REGISTRATE
            .item("nuke_icon", Item::new)
            .removeTab(CreativeModeTabs.SEARCH)
            .register();

    public static void register() {
    }
}

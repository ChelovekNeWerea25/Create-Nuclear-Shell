package com.greenmods.canonnukes.item;

import com.greenmods.canonnukes.CanonNukes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class ModItems {
    private static final CreateRegistrate REGISTRATE = CanonNukes.REGISTRATE;

    public static final ItemEntry<Item> NUKE_ICON = REGISTRATE
            .item("nuke_icon", Item::new)
            .register();


    public static void register() {
    }
}

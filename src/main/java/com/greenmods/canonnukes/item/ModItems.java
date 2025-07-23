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

    public static final ItemEntry<Item> STEEL_DUST = REGISTRATE
            .item("steel_dust", Item::new)
            .register();
    public static final ItemEntry<Item> STEEL_INGOT = REGISTRATE
            .item("steel_ingot", Item::new)
            .register();
    public static final ItemEntry<Item> STEEL_PLATE = REGISTRATE
            .item("steel_plate", Item::new)
            .register();

    public static final ItemEntry<Item> URAN_WRENCH = REGISTRATE
            .item("steel_wrench", Item::new)
            .register();
    public static final ItemEntry<Item> RAW_URAN = REGISTRATE
            .item("raw_uran", Item::new)
            .register();
    public static final ItemEntry<Item> URAN_NUGGET =  REGISTRATE
            .item("uran_nugget", Item::new)
            .register();
    public static final ItemEntry<Item> URAN_INGOT = REGISTRATE
            .item("uran_ingot", Item::new)
            .register();

    public static final ItemEntry<Item> PROCESSED_URAN = REGISTRATE
            .item("enriched_uran_dust", Item::new)
            .register();
    public static void register() {
    }
}

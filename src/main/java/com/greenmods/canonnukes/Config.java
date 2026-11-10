package com.greenmods.canonnukes;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Configuration of the nuclear shell.
 *
 * <p>The values are read directly from the {@link ModConfigSpec}, NeoForge keeps them in sync
 * between the client and the server, so the same values are used on both sides.</p>
 */
public class Config {

    /** Blast radius of a detonating nuclear shell, in blocks. */
    public static final ModConfigSpec.DoubleValue BLAST_RADIUS;

    /** Multiplier for the damage the blast deals to entities. */
    public static final ModConfigSpec.DoubleValue DAMAGE_MULTIPLIER;

    /** Whether the blast destroys blocks and digs a crater. */
    public static final ModConfigSpec.BooleanValue DESTROY_BLOCKS;

    /** Amount of ticks the crater is dug over. */
    public static final ModConfigSpec.IntValue CRATER_STEPS;

    /** Amount of ticks between two steps of the crater. */
    public static final ModConfigSpec.IntValue CRATER_STEP_INTERVAL;

    /** Whether the blast irradiates entities. */
    public static final ModConfigSpec.BooleanValue RADIATION;

    /** Duration of the radiation effect in ticks (20 ticks = 1 second). */
    public static final ModConfigSpec.IntValue RADIATION_DURATION;

    /** Whether the blast shakes the screen of nearby players. */
    public static final ModConfigSpec.BooleanValue SCREEN_SHAKE;

    /** Whether the mushroom cloud is rendered. */
    public static final ModConfigSpec.BooleanValue MUSHROOM_CLOUD;

    public static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Nuclear shell explosion").push("explosion");

        BLAST_RADIUS = builder
                .comment("Blast radius of the nuclear shell in blocks")
                .defineInRange("blastRadius", 35.0D, 1.0D, 128.0D);

        DAMAGE_MULTIPLIER = builder
                .comment("Multiplier for the damage dealt to entities by the blast")
                .defineInRange("damageMultiplier", 1.0D, 0.0D, 10.0D);

        DESTROY_BLOCKS = builder
                .comment("Whether the blast destroys blocks and digs a crater")
                .define("destroyBlocks", true);

        CRATER_STEPS = builder
                .comment("Amount of steps the crater is dug over, higher values look smoother but take longer")
                .defineInRange("craterSteps", 40, 1, 200);

        CRATER_STEP_INTERVAL = builder
                .comment("Amount of ticks between two steps of the crater")
                .defineInRange("craterStepInterval", 2, 1, 20);

        builder.pop();

        builder.comment("Nuclear shell effects").push("effects");

        RADIATION = builder
                .comment("Whether the blast irradiates entities")
                .define("radiation", true);

        RADIATION_DURATION = builder
                .comment("Duration of the radiation effect in ticks, 20 ticks are one second")
                .defineInRange("radiationDuration", 800, 0, 20000);

        SCREEN_SHAKE = builder
                .comment("Whether the blast shakes the screen of nearby players")
                .define("screenShake", true);

        MUSHROOM_CLOUD = builder
                .comment("Whether the mushroom cloud is rendered")
                .define("mushroomCloud", true);

        builder.pop();

        SPEC = builder.build();
    }

    private Config() {
    }
}

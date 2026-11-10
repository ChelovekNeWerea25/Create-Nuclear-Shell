package com.greenmods.canonnukes.commands;

import com.greenmods.canonnukes.world.explosion.NukeExplosion;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public class NukeCommand {

    /** Default blast radius of the {@code /nuke} command. */
    public static final float DEFAULT_RADIUS = 20F;

    /** Highest blast radius the command accepts. */
    public static final float MAX_RADIUS = 60F;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("nuke")
                        .requires(source -> source.hasPermission(2))

                        .executes(context -> spawnNuke(context, DEFAULT_RADIUS))

                        .then(Commands.argument("radius", FloatArgumentType.floatArg(1.0F, MAX_RADIUS))
                                .executes(context -> spawnNuke(
                                        context,
                                        FloatArgumentType.getFloat(context, "radius")
                                ))
                        )
        );
    }

    private static int spawnNuke(CommandContext<CommandSourceStack> context, float radius) {
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();
        Vec3 pos = source.getPosition();

        NukeExplosion nuke = new NukeExplosion(
                level,
                source.getPlayer(),
                null,
                null,
                pos.x, pos.y, pos.z,
                radius,
                true,
                Explosion.BlockInteraction.DESTROY
        );

        nuke.explode();

        nuke.finalizeExplosion(true);

        source.sendSuccess(
                () -> Component.translatable("commands.canonnukes.nuke.success", Math.round(radius))
                        .withStyle(ChatFormatting.RED),
                true
        );

        return 1;
    }
}

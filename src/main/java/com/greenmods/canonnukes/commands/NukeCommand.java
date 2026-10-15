package com.greenmods.canonnukes.commands;

import com.greenmods.canonnukes.world.explosion.NukeExplosion;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public class NukeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("nuke")
                        .requires(source -> source.hasPermission(2))

                        .executes(context -> spawnNuke(context, 20.0F))

                        .then(Commands.argument("radius", FloatArgumentType.floatArg(1.0F, 150.0F))
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

        ServerPlayer player = source.getPlayer();

        NukeExplosion nuke = new NukeExplosion(
                level,
                player,
                null,
                null,
                pos.x, pos.y, pos.z,
                radius,
                true,
                Explosion.BlockInteraction.DESTROY
        );

        nuke.explode();

        nuke.finalizeExplosion(true);

        source.sendSuccess(() -> Component.literal("§c[!] Ядерный взрыв (R=" + radius + ") активирован!"), true);
        return 1;
    }
}

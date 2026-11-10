package com.greenmods.canonnukes.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.server.MinecraftServer;

/**
 * Simple server side scheduler.
 *
 * <p>{@code MinecraftServer#tell(TickTask)} and {@code MinecraftServer#execute(Runnable)} run the
 * given task <em>immediately</em> when they are called from the server thread, so they cannot be
 * used to spread work over multiple ticks. This scheduler keeps the pending tasks around and runs
 * them from the server tick handler once their due tick has been reached.</p>
 */
public final class TickScheduler {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final List<ScheduledTask> TASKS = new ArrayList<>();

    private TickScheduler() {
    }

    /**
     * Schedules a task to be run on the server thread after the given amount of ticks.
     *
     * @param server     the server the task belongs to
     * @param delayTicks amount of ticks to wait, {@code 0} runs the task on the next tick
     * @param task       the task to run
     */
    public static void schedule(MinecraftServer server, int delayTicks, Runnable task) {
        TASKS.add(new ScheduledTask(server.getTickCount() + Math.max(1, delayTicks), task));
    }

    /**
     * Runs every task that is due. Has to be called from the server thread.
     */
    public static void runPendingTasks(MinecraftServer server) {
        if (TASKS.isEmpty()) {
            return;
        }

        int currentTick = server.getTickCount();
        Iterator<ScheduledTask> iterator = TASKS.iterator();

        while (iterator.hasNext()) {
            ScheduledTask scheduled = iterator.next();

            if (scheduled.dueTick() > currentTick) {
                continue;
            }

            iterator.remove();

            try {
                scheduled.task().run();
            } catch (Exception e) {
                LOGGER.error("Failed to run scheduled task", e);
            }
        }
    }

    /**
     * Drops every pending task, used when the server shuts down.
     */
    public static void clear() {
        TASKS.clear();
    }

    private record ScheduledTask(int dueTick, Runnable task) {
    }
}

package com.greenmods.canonnukes.world.explosion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NukeExplosion extends Explosion {

    private final Level level;
    private final double x, y, z;
    private final float radius;
    private final boolean fire;

    private final List<BlockPos> sortedBlocks = new ArrayList<>();

    public NukeExplosion(Level level,
                         @Nullable Entity source,
                         @Nullable net.minecraft.world.damagesource.DamageSource damageSource,
                         @Nullable ExplosionDamageCalculator damageCalculator,
                         double x, double y, double z,
                         float radius,
                         boolean fire,
                         BlockInteraction blockInteraction) {
        super(level, source, damageSource, damageCalculator, x, y, z, radius, fire, blockInteraction);
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.fire = fire;
    }

    @Override
    public void explode() {

        if (this.interactsWithBlocks()) {
            int r = Math.round(this.radius);
            float radiusSq = this.radius * this.radius;
            BlockPos center = BlockPos.containing(this.x, this.y, this.z);

            List<BlockPos> temp = new ArrayList<>();

            for (int dx = -r; dx <= r; dx++) {
                for (int dy = -r; dy <= r; dy++) {
                    for (int dz = -r; dz <= r; dz++) {
                        double distSq = dx * dx + dy * dy + dz * dz;

                        if (distSq > radiusSq) continue;

                        double dist = Math.sqrt(distSq);
                        double edgeFactor = dist / this.radius;

                        double noise = (this.level.random.nextDouble() - 0.5) * 0.35;
                        double effectiveRadius = this.radius * (1.0 + noise * edgeFactor);

                        if (dist > effectiveRadius) continue;

                        BlockPos pos = center.offset(dx, dy, dz);
                        if (!this.level.isInWorldBounds(pos)) continue;

                        BlockState state = this.level.getBlockState(pos);
                        if (state.isAir() || state.getBlock() == Blocks.BEDROCK) continue;

                        temp.add(pos);
                    }
                }
            }

            temp.sort((a, b) -> {
                double da = a.distToCenterSqr(this.x, this.y, this.z);
                double db = b.distToCenterSqr(this.x, this.y, this.z);
                return Double.compare(db, da);
            });

            this.sortedBlocks.addAll(temp);
            this.getToBlow().addAll(temp);
        }

        float effectRadius = this.radius * 2.5F;
        float effectRadiusSq = effectRadius * effectRadius;

        AABB box = new AABB(
                this.x - effectRadius, this.y - effectRadius, this.z - effectRadius,
                this.x + effectRadius, this.y + effectRadius, this.z + effectRadius
        );

        List<Entity> entities = this.level.getEntities(this.getIndirectSourceEntity(), box);
        Vec3 centerVec = new Vec3(this.x, this.y, this.z);

        for (Entity entity : entities) {
            if (entity.ignoreExplosion()) continue;

            double distSq = entity.distanceToSqr(centerVec);
            if (distSq > effectRadiusSq) continue;

            double dist = Math.sqrt(distSq);
            if (dist < 0.001) dist = 0.001;

            double dirX = (entity.getX() - this.x) / dist;
            double dirY = (entity.getEyeY() - this.y) / dist;
            double dirZ = (entity.getZ() - this.z) / dist;

            double factor = Math.max(0.0, 1.0 - (dist / effectRadius));

            float damage = (float) (factor * factor * 180.0F + 25.0F);
            entity.hurt(this.getDamageSource(), damage);

            double pushForce = factor * 6.8D;
            Vec3 impulse = new Vec3(dirX * pushForce, (dirY * pushForce) + 1.2D, dirZ * pushForce);
            entity.setDeltaMovement(entity.getDeltaMovement().add(impulse));

            if (entity instanceof Player player) {
                if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                    this.getHitPlayers().put(player, impulse);
                }
            }

            if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.WITHER, 800, 5));
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 400, 2));
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 3));
                living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 250, 1));

                if (this.fire) {
                    living.setSecondsOnFire((int) (factor * 20 + 5));
                }
            }
        }
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        if (!(this.level instanceof ServerLevel serverLevel)) return;

        serverLevel.playSound(null, this.x, this.y, this.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 18.0F, 0.42F);
        serverLevel.playSound(null, this.x, this.y, this.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 13.0F, 0.7F);
        serverLevel.playSound(null, this.x, this.y, this.z,
                SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 15.0F, 0.32F);
        serverLevel.playSound(null, this.x, this.y, this.z,
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 9.0F, 0.38F);

        spawnMushroomCloud(serverLevel);

        progressiveBlockRemoval(serverLevel);

        float shakeRadius = this.radius * 4.5F;
        for (ServerPlayer player : serverLevel.players()) {
            double distSq = player.distanceToSqr(this.x, this.y, this.z);
            if (distSq >= shakeRadius * shakeRadius) continue;

            float intensity = (float) (1.0 - Math.sqrt(distSq) / shakeRadius);

            for (int i = 0; i < 9; i++) {
                final int delay = i * 2;
                final float force = intensity * (1.0f - i * 0.1f);

                level.getServer().tell(new net.minecraft.server.TickTask(
                        level.getServer().getTickCount() + delay,
                        () -> {
                            if (!player.isAlive() || player.isSpectator()) return;

                            Vec3 shake = new Vec3(
                                    (level.random.nextDouble() - 0.5) * force * 1.5,
                                    force * 0.3,
                                    (level.random.nextDouble() - 0.5) * force * 1.5
                            );
                            player.setDeltaMovement(player.getDeltaMovement().add(shake));
                            player.hurtMarked = true;
                        }
                ));
            }
        }
    }

    private void progressiveBlockRemoval(ServerLevel level) {
        if (sortedBlocks.isEmpty()) return;

        int total = sortedBlocks.size();
        int perStep = Math.max(40, total / 35);

        for (int step = 0; step < 40; step++) {
            final int currentStep = step;

            level.getServer().tell(new net.minecraft.server.TickTask(
                    level.getServer().getTickCount() + step * 2,
                    () -> {
                        int start = currentStep * perStep;
                        int end = Math.min(start + perStep, total);

                        for (int i = start; i < end; i++) {
                            BlockPos pos = sortedBlocks.get(i);
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);

                            if (this.fire && level.random.nextInt(7) == 0) {
                                if (level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
                                    level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                                }
                            }
                        }
                    }
            ));
        }
    }

    private void spawnMushroomCloud(ServerLevel level) {
        final double cx = this.x;
        final double cy = this.y;
        final double cz = this.z;
        final float r = this.radius;

        final double stemHeight = Math.max(28, r * 3.8);
        final double capRadius  = Math.max(13, r * 2.2);
        final double fireballR  = Math.max(7, r * 1.0);

        level.sendParticles(ParticleTypes.FLASH, cx, cy + 1.8, cz, 12, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, cx, cy + 2.5, cz, 22, fireballR * 0.6, fireballR * 0.4, fireballR * 0.6, 0);

        level.sendParticles(ParticleTypes.FLAME, cx, cy + 2.2, cz,
                (int)(450 + r * 30), fireballR, fireballR * 0.75, fireballR, 0.42);

        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, cx, cy + 2.8, cz,
                (int)(160 + r * 12), fireballR * 0.75, fireballR * 0.45, fireballR * 0.75, 0.18);

        level.sendParticles(ParticleTypes.END_ROD, cx, cy + 3.5, cz,
                (int)(160 + r * 16), fireballR * 1.1, fireballR * 0.65, fireballR * 1.1, 0.28);

        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 3, () -> {
            for (int ring = 1; ring <= 9; ring++) {
                double radius = fireballR * 1.45 * ring;
                int points = 64 + ring * 18;

                for (int i = 0; i < points; i++) {
                    double angle = (i / (double) points) * Math.PI * 2.0;
                    double px = cx + Math.cos(angle) * radius;
                    double pz = cz + Math.sin(angle) * radius;


                    level.sendParticles(ParticleTypes.CLOUD, px, cy + 1.7, pz, 5, 0.55, 0.4, 0.55, 0.018);
                    level.sendParticles(ParticleTypes.SMOKE, px, cy + 1.4, pz, 4, 0.4, 0.25, 0.4, 0.012);


                    if (ring % 2 == 0) {
                        level.sendParticles(ParticleTypes.LARGE_SMOKE, px, cy + 1.9, pz, 3, 0.45, 0.3, 0.45, 0.01);
                    }
                    if (ring % 3 == 0) {
                        level.sendParticles(ParticleTypes.POOF, px, cy + 1.5, pz, 2, 0.35, 0.2, 0.35, 0.015);
                    }
                }
            }

            level.sendParticles(ParticleTypes.POOF, cx, cy + 1.2, cz, 90, r * 0.9, 2.0, r * 0.9, 0.1);
            level.sendParticles(ParticleTypes.CLOUD, cx, cy + 1.5, cz, 70, r * 0.7, 1.8, r * 0.7, 0.08);
        }));

        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 9, () -> {
            int steps = (int) (stemHeight / 1.15);

            for (int i = 0; i < steps; i++) {
                double h = i * 1.15;
                double width = 2.1 + i * 0.13;

                level.sendParticles(ParticleTypes.LARGE_SMOKE,
                        cx, cy + h, cz, 28, width, 0.55, width, 0.016);

                level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                        cx, cy + h, cz, 18, width * 0.65, 0.4, width * 0.65, 0.012);

                if (i % 2 == 0) {
                    level.sendParticles(ParticleTypes.END_ROD,
                            cx, cy + h, cz, 5, width * 0.35, 0.25, width * 0.35, 0.04);
                }
            }
        }));

        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 22, () -> {
            double capY = cy + stemHeight;

            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    cx, capY, cz, (int)(190 + r * 20), capRadius, 4.5, capRadius, 0.028);

            level.sendParticles(ParticleTypes.CLOUD,
                    cx, capY + 1.2, cz, (int)(120 + r * 14), capRadius * 1.0, 3.5, capRadius * 1.0, 0.022);

            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                    cx, capY, cz, (int)(90 + r * 10), capRadius * 0.5, 2.8, capRadius * 0.5, 0.02);

            int ringPoints = (int)(70 + r * 5);
            for (int i = 0; i < ringPoints; i++) {
                double angle = (i / (double) ringPoints) * Math.PI * 2.0;
                double rx = Math.cos(angle) * capRadius * 0.95;
                double rz = Math.sin(angle) * capRadius * 0.95;

                level.sendParticles(ParticleTypes.CLOUD,
                        cx + rx, capY + 2.5, cz + rz, 10, 2.3, 1.6, 2.3, 0.02);

                level.sendParticles(ParticleTypes.END_ROD,
                        cx + rx, capY + 2.8, cz + rz, 4, 1.4, 1.0, 1.4, 0.05);
            }

            level.sendParticles(ParticleTypes.END_ROD, cx, capY + 2.2, cz, 70, 4.0, 2.5, 4.0, 0.1);
            level.sendParticles(ParticleTypes.FLASH, cx, capY + 1.8, cz, 4, 0, 0, 0, 0);
        }));

        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 45, () -> {
            double capY = cy + stemHeight;

            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    cx, capY - 6, cz, (int)(220 + r * 18), capRadius * 1.7, 6.5, capRadius * 1.7, 0.035);

            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    cx, cy + 3, cz, (int)(140 + r * 14), r * 1.6, 4.0, r * 1.6, 0.03);

            level.sendParticles(ParticleTypes.END_ROD,
                    cx, capY + 1, cz, 90, capRadius * 1.0, 5.5, capRadius * 1.0, 0.05);

            level.sendParticles(ParticleTypes.ASH,
                    cx, cy + 5, cz, 100, r * 1.4, 5.0, r * 1.4, 0.025);
        }));
    }
}
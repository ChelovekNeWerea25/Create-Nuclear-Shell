package com.greenmods.canonnukes.block.nuke;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NuclearBombRenderer extends EntityRenderer<NuclearExplosionEntity> {

    public NuclearBombRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(NuclearExplosionEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Здесь будет код рендеринга
        // Можно добавить частицы, спецэффекты или модель
    }

    @Override
    public ResourceLocation getTextureLocation(NuclearExplosionEntity entity) {
        // Возвращаем пустую текстуру, так как мы не используем стандартную модель
        return null;
    }
}
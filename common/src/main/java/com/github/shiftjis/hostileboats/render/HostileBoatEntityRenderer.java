package com.github.shiftjis.hostileboats.render;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class HostileBoatEntityRenderer extends MobEntityRenderer<HostileBoatEntity, HostileBoatModel> {
    public HostileBoatEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HostileBoatModel(context.getPart(EntityModelLayers.createBoat(BoatEntity.Type.OAK))), 0.75F);
    }

    @Override
    public void render(HostileBoatEntity boatEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        matrixStack.translate(0F, 0.375F, 0F);

        float headPitch = MathHelper.lerp(tickDelta, boatEntity.prevPitch, boatEntity.getPitch());
        if (shouldFlipUpsideDown(boatEntity)) {
            headPitch *= -1F;
        }

        float animationProgress = getAnimationProgress(boatEntity, tickDelta);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F - yaw));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180F + headPitch));
        model.setAngles(boatEntity, tickDelta, 0F, -0.1F, 0, headPitch);

        MinecraftClient instance = MinecraftClient.getInstance();
        boolean isVisible = !isVisible(boatEntity) && !boatEntity.isInvisibleTo(instance.player);
        RenderLayer renderLayer = getRenderLayer(boatEntity, isVisible(boatEntity), isVisible, instance.hasOutline(boatEntity));
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
            int overlay = getOverlay(boatEntity, getAnimationCounter(boatEntity, tickDelta));
            model.render(matrixStack, vertexConsumer, light, overlay, 1F, 1F, 1F, isVisible ? 0.15F : 1F);
        }

        if (!boatEntity.isSpectator()) {
            for (FeatureRenderer<HostileBoatEntity, HostileBoatModel> renderer : features) {
                renderer.render(matrixStack, vertexConsumers, light, boatEntity, 0, 0, tickDelta, animationProgress, 0, headPitch);
            }
        }

        matrixStack.pop();
        if (hasLabel(boatEntity)) {
            renderLabelIfPresent(boatEntity, boatEntity.getDisplayName(), matrixStack, vertexConsumers, light);
        }
    }

    @Override
    public Identifier getTexture(HostileBoatEntity entity) {
        return new Identifier("textures/entity/boat/oak.png");
    }
}

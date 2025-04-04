package com.github.shiftjis.hostileboats.renderer;

import com.github.shiftjis.hostileboats.entities.HostileBoatEntity;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class HostileBoatModel extends CompositeEntityModel<HostileBoatEntity> {
    private final ImmutableList<ModelPart> modelParts;
    private final ModelPart rightPaddle;
    private final ModelPart leftPaddle;

    public HostileBoatModel(ModelPart root) {
        this.rightPaddle = root.getChild("right_paddle");
        this.leftPaddle = root.getChild("left_paddle");
        this.modelParts = getParts(root);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return modelParts;
    }

    @Override
    public void setAngles(HostileBoatEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        setPaddleAngle(entity, 0, leftPaddle, limbAngle);
        setPaddleAngle(entity, 1, rightPaddle, limbAngle);
    }

    private ImmutableList<ModelPart> getParts(ModelPart root) {
        return ImmutableList.of(root.getChild("bottom"), root.getChild("back"), root.getChild("front"), root.getChild("right"), root.getChild("left"), leftPaddle, rightPaddle);
    }

    private void setPaddleAngle(HostileBoatEntity entity, int sigma, ModelPart part, float angle) {
        float paddlePhase = entity.interpolatePaddlePhase(sigma, angle);
        part.pitch = MathHelper.clampedLerp((-(float) Math.PI / 3F), -0.2617994F, (MathHelper.sin(-paddlePhase) + 1F) / 2F);
        part.yaw = MathHelper.clampedLerp((-(float) Math.PI / 4F), ((float) Math.PI / 4F), (MathHelper.sin(-paddlePhase + 1F) + 1F) / 2F);
        if (sigma == 1) {
            part.yaw = (float) Math.PI - part.yaw;
        }
    }
}

package com.github.shiftjis.hostileboats.entity.controls;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BoatMoveControl extends MoveControl {
    private final HostileBoatEntity hostileBoat;
    private float targetSpeed = 0.1F;

    public BoatMoveControl(HostileBoatEntity hostileBoat) {
        super(hostileBoat);
        this.hostileBoat = hostileBoat;
    }

    @Override
    public void tick() {
        if (hostileBoat.horizontalCollision) {
            hostileBoat.setYaw(hostileBoat.getYaw() + 180F);
            targetSpeed = 0.1F;
        }

        double deltaX = hostileBoat.targetPosition.x - hostileBoat.getX();
        double deltaY = hostileBoat.targetPosition.y - hostileBoat.getY();
        double deltaZ = hostileBoat.targetPosition.z - hostileBoat.getZ();
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        if (Math.abs(horizontalDistance) > 1.0E-5) {
            double verticalInfluenceFactor = 1 - Math.abs(deltaY * 0.7) / horizontalDistance;
            deltaX *= verticalInfluenceFactor;
            deltaZ *= verticalInfluenceFactor;

            horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            double totalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ + deltaY * deltaY);

            float previousYaw = hostileBoat.getYaw();
            float targetYawRadians = (float) MathHelper.atan2(deltaZ, deltaX);
            float currentSidewaysYaw = MathHelper.wrapDegrees(hostileBoat.getYaw() + 90F);
            float targetYawDegrees = MathHelper.wrapDegrees(targetYawRadians * (180F / (float) Math.PI));
            hostileBoat.setYaw(MathHelper.stepUnwrappedAngleTowards(currentSidewaysYaw, targetYawDegrees, 4F) - 90F);
            hostileBoat.bodyYaw = hostileBoat.getYaw();

            if (MathHelper.angleBetween(previousYaw, hostileBoat.getYaw()) < 3.0F) {
                targetSpeed = MathHelper.stepTowards(targetSpeed, 1.8F, 0.005F * (1.8F / targetSpeed));
            } else {
                targetSpeed = MathHelper.stepTowards(targetSpeed, 0.2F, 0.025F);
            }

            float targetPitchDegrees = (float) (-(MathHelper.atan2(-deltaY, horizontalDistance) * (180F / Math.PI)));
            hostileBoat.setPitch(targetPitchDegrees);

            float sidewaysYawForVelocity = hostileBoat.getYaw() + 90F;
            double velocityX = (targetSpeed * MathHelper.cos(sidewaysYawForVelocity * ((float) Math.PI / 180F))) * Math.abs(deltaX / totalDistance);
            double velocityZ = (targetSpeed * MathHelper.sin(sidewaysYawForVelocity * ((float) Math.PI / 180F))) * Math.abs(deltaZ / totalDistance);
            double velocityY = (targetSpeed * MathHelper.sin(targetPitchDegrees * ((float) Math.PI / 180F))) * Math.abs(deltaY / totalDistance);

            Vec3d currentVelocity = hostileBoat.getVelocity();
            hostileBoat.setVelocity(currentVelocity.add((new Vec3d(velocityX, velocityY, velocityZ)).subtract(currentVelocity).multiply(0.2)));
        }
    }
}

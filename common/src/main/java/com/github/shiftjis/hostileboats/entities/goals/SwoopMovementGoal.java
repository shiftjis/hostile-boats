package com.github.shiftjis.hostileboats.entities.goals;

import com.github.shiftjis.hostileboats.entities.HostileBoatEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

// reference: net.minecraft.entity.mob.PhantomEntity
public class SwoopMovementGoal extends BoatMovementGoal {
    public SwoopMovementGoal(HostileBoatEntity hostileBoat) {
        super(hostileBoat);
    }

    @Override
    public void stop() {
        hostileBoat.setTarget(null);
        hostileBoat.movementType = HostileBoatEntity.BoatMovementType.CIRCLE;
        super.start();
    }

    @Override
    public void tick() {
        LivingEntity targetEntity = hostileBoat.getTarget();
        if (targetEntity != null) {
            hostileBoat.targetPosition = new Vec3d(targetEntity.getX(), targetEntity.getBodyY(0.5F), targetEntity.getZ());
            if (hostileBoat.getBoundingBox().expand(0.2F).intersects(targetEntity.getBoundingBox())) {
                hostileBoat.tryAttack(targetEntity);
                hostileBoat.movementType = HostileBoatEntity.BoatMovementType.CIRCLE;
            } else if (hostileBoat.horizontalCollision || hostileBoat.hurtTime > 0) {
                hostileBoat.movementType = HostileBoatEntity.BoatMovementType.CIRCLE;
            }
        }
    }

    @Override
    public boolean canStart() {
        return hostileBoat.getTarget() != null && hostileBoat.movementType == HostileBoatEntity.BoatMovementType.SWOOP;
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity targetEntity = hostileBoat.getTarget();
        if (targetEntity == null || !targetEntity.isAlive()) {
            return false;
        }

        if (targetEntity instanceof PlayerEntity playerEntity && (targetEntity.isSpectator() || playerEntity.isCreative())) {
            return false;
        }

        return canStart();
    }
}

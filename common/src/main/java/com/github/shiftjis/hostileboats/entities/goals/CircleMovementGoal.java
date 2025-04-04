package com.github.shiftjis.hostileboats.entities.goals;

import com.github.shiftjis.hostileboats.entities.HostileBoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

// reference: net.minecraft.entity.mob.PhantomEntity
public class CircleMovementGoal extends BoatMovementGoal {
    private float direction;
    private float yOffset;
    private float radius;
    private float angle;

    public CircleMovementGoal(HostileBoatEntity hostileBoat) {
        super(hostileBoat);
    }

    @Override
    public void start() {
        direction = hostileBoat.boatRandom.nextBoolean() ? 1F : -1F;
        yOffset = -4F + hostileBoat.boatRandom.nextFloat() * 9F;
        radius = 5F + hostileBoat.boatRandom.nextFloat() * 10F;
        adjustDirection();
        super.start();
    }

    @Override
    public void tick() {
        if (hostileBoat.boatRandom.nextInt(getTickCount(350)) == 0) {
            yOffset = -4.0F + hostileBoat.boatRandom.nextFloat() * 9F;
        }

        if (hostileBoat.boatRandom.nextInt(getTickCount(250)) == 0 && ++radius > 15F) {
            direction = -direction;
            radius = 5F;
        }

        if (hostileBoat.boatRandom.nextInt(getTickCount(450)) == 0) {
            angle = hostileBoat.boatRandom.nextFloat() * 2F * (float) Math.PI;
            adjustDirection();
        }

        if (isNearTarget()) {
            adjustDirection();
        }

        if (hostileBoat.targetPosition.y < hostileBoat.getY() && !hostileBoat.getWorld().isAir(hostileBoat.getBlockPos().down(1))) {
            yOffset = Math.max(1F, yOffset);
            adjustDirection();
        }

        if (hostileBoat.targetPosition.y > hostileBoat.getY() && !hostileBoat.getWorld().isAir(hostileBoat.getBlockPos().up(1))) {
            yOffset = Math.min(-1F, yOffset);
            adjustDirection();
        }
    }

    @Override
    public boolean canStart() {
        return hostileBoat.getTarget() == null || hostileBoat.movementType == HostileBoatEntity.BoatMovementType.CIRCLE;
    }

    private void adjustDirection() {
        if (BlockPos.ORIGIN.equals(hostileBoat.circlingCenter)) {
            hostileBoat.circlingCenter = hostileBoat.getBlockPos();
        }

        angle += direction * 15F * ((float) Math.PI / 180F);
        hostileBoat.targetPosition = Vec3d.of(hostileBoat.circlingCenter).add(radius * MathHelper.cos(angle), -4F + yOffset, radius * MathHelper.sin(angle));
    }
}

package com.github.shiftjis.hostileboats.entity.goals;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

// reference: net.minecraft.entity.mob.PhantomEntity
public class StartAttackGoal extends Goal {
    private final HostileBoatEntity hostileBoat;
    private int cooldownTicks;

    public StartAttackGoal(HostileBoatEntity hostileBoat) {
        this.hostileBoat = hostileBoat;
    }

    @Override
    public void start() {
        hostileBoat.movementType = HostileBoatEntity.BoatMovementType.CIRCLE;
        cooldownTicks = getTickCount(10);
        startSwoop();
    }

    @Override
    public void stop() {
        hostileBoat.circlingCenter = hostileBoat.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING, hostileBoat.circlingCenter).up(10 + hostileBoat.boatRandom.nextInt(20));
    }

    @Override
    public void tick() {
        if (hostileBoat.movementType == HostileBoatEntity.BoatMovementType.CIRCLE && --cooldownTicks <= 0) {
            hostileBoat.movementType = HostileBoatEntity.BoatMovementType.SWOOP;
            cooldownTicks = getTickCount((8 + hostileBoat.boatRandom.nextInt(4)) * 20);
            startSwoop();
        }
    }

    @Override
    public boolean canStart() {
        LivingEntity targetEntity = hostileBoat.getTarget();
        return targetEntity != null && hostileBoat.isTarget(targetEntity, TargetPredicate.DEFAULT);
    }

    private void startSwoop() {
        hostileBoat.circlingCenter = hostileBoat.getTarget().getBlockPos().up(20 + hostileBoat.boatRandom.nextInt(20));
        if (hostileBoat.circlingCenter.getY() < hostileBoat.getWorld().getSeaLevel()) {
            hostileBoat.circlingCenter = new BlockPos(hostileBoat.circlingCenter.getX(), hostileBoat.getWorld().getSeaLevel() + 1, hostileBoat.circlingCenter.getZ());
        }
    }
}

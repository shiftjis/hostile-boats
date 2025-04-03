package com.github.shiftjis.hostileboats.entity.goals;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

// reference: net.minecraft.entity.mob.PhantomEntity
public abstract class BoatMovementGoal extends Goal {
    protected final HostileBoatEntity hostileBoat;

    public BoatMovementGoal(HostileBoatEntity hostileBoat) {
        setControls(EnumSet.of(Goal.Control.MOVE));
        this.hostileBoat = hostileBoat;
    }

    @Override
    public void start() {
        hostileBoat.setPaddleMovings(true, true);
    }

    @Override
    public void stop() {
        hostileBoat.setPaddleMovings(false, false);
    }

    public boolean isNearTarget() {
        return hostileBoat.targetPosition.squaredDistanceTo(hostileBoat.getX(), hostileBoat.getY(), hostileBoat.getZ()) < 4.0;
    }
}

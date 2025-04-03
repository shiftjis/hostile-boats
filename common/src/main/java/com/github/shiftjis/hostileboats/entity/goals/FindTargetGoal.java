package com.github.shiftjis.hostileboats.entity.goals;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;

// reference: net.minecraft.entity.mob.PhantomEntity
public class FindTargetGoal extends Goal {
    private final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(64);
    private final HostileBoatEntity hostileBoat;
    private int searchDelayTicks = toGoalTicks(20);

    public FindTargetGoal(HostileBoatEntity hostileBoat) {
        this.hostileBoat = hostileBoat;
    }

    @Override
    public boolean canStart() {
        if (searchDelayTicks > 0) {
            --searchDelayTicks;
        } else {
            searchDelayTicks = toGoalTicks(60);

            Box searchBox = hostileBoat.getBoundingBox().expand(16, 64, 16);
            List<PlayerEntity> nearbyPlayers = hostileBoat.getWorld().getPlayers(PLAYERS_IN_RANGE_PREDICATE, hostileBoat, searchBox);
            if (!nearbyPlayers.isEmpty()) {
                nearbyPlayers.sort(Comparator.comparing(Entity::getY).reversed());
                for (PlayerEntity playerEntity : nearbyPlayers) {
                    if (hostileBoat.isTarget(playerEntity, TargetPredicate.DEFAULT)) {
                        hostileBoat.setTarget(playerEntity);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

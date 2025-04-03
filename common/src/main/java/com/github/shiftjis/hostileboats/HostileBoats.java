package com.github.shiftjis.hostileboats;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;

public final class HostileBoats {
    public static final EntityType.Builder<HostileBoatEntity> BOAT_ENTITY_BUILDER = EntityType.Builder.create(HostileBoatEntity::new, SpawnGroup.CREATURE).setDimensions(1.375F, 0.5625F);
    public static final String MOD_ID = "hostile_boats";

    public static DefaultAttributeContainer.Builder createBoatAttributes() {
        DefaultAttributeContainer.Builder builder = HostileBoatEntity.createMobAttributes();
        builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
        return builder;
    }
}

package com.github.shiftjis.hostileboats.entity.controls;

import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;

public class BoatLookControl extends LookControl {
    public BoatLookControl(MobEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
    }
}

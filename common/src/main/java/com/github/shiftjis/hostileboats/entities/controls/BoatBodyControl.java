package com.github.shiftjis.hostileboats.entities.controls;

import com.github.shiftjis.hostileboats.entities.HostileBoatEntity;
import net.minecraft.entity.ai.control.BodyControl;

public class BoatBodyControl extends BodyControl {
    private final HostileBoatEntity hostileBoat;

    public BoatBodyControl(HostileBoatEntity hostileBoat) {
        super(hostileBoat);
        this.hostileBoat = hostileBoat;
    }

    @Override
    public void tick() {
        hostileBoat.headYaw = hostileBoat.bodyYaw;
        hostileBoat.bodyYaw = hostileBoat.getYaw();
    }
}

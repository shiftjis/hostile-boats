package com.github.shiftjis.hostileboats.fabric;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import net.fabricmc.api.ModInitializer;

import com.github.shiftjis.hostileboats.HostileBoats;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class HostileBoatsFabric implements ModInitializer {
    public static final EntityType<HostileBoatEntity> BOAT_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of("hostile_boats", "hostile_boat"), HostileBoats.BOAT_ENTITY_BUILDER.build("hostile_boats"));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(BOAT_ENTITY_TYPE, HostileBoats.createBoatAttributes());
    }
}

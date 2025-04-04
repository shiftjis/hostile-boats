package com.github.shiftjis.hostileboats.fabric;

import com.github.shiftjis.hostileboats.entities.HostileBoatEntity;
import net.fabricmc.api.ModInitializer;

import com.github.shiftjis.hostileboats.HostileBoats;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

public final class HostileBoatsFabric implements ModInitializer {
    public static final EntityType<HostileBoatEntity> BOAT_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of("hostile_boats", "hostile_boat"), HostileBoats.BOAT_ENTITY_BUILDER.build("hostile_boats"));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(BOAT_ENTITY_TYPE, HostileBoats.createBoatAttributes());
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.MONSTER, BOAT_ENTITY_TYPE, 15, 1, 3);
        SpawnRestriction.register(BOAT_ENTITY_TYPE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.WORLD_SURFACE, MobEntity::canMobSpawn);
    }
}

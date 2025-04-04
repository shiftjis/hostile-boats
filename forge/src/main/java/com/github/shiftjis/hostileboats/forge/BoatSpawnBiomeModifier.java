package com.github.shiftjis.hostileboats.forge;

import com.github.shiftjis.hostileboats.HostileBoats;
import com.mojang.serialization.Codec;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BoatSpawnBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new Identifier(HostileBoats.MOD_ID, "hostile_boat_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, HostileBoats.MOD_ID);

    @Override
    public void modify(RegistryEntry<Biome> arg, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && arg.containsTag(BiomeTags.IS_OVERWORLD)) {
            builder.getMobSpawnSettings().spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(HostileBoatsForge.BOAT_ENTITY_TYPE.get(), 15, 1, 3));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<BoatSpawnBiomeModifier> makeCodec() {
        return Codec.unit(BoatSpawnBiomeModifier::new);
    }
}

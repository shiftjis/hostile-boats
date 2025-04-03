package com.github.shiftjis.hostileboats.forge;

import com.github.shiftjis.hostileboats.entity.HostileBoatEntity;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.github.shiftjis.hostileboats.HostileBoats;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(HostileBoats.MOD_ID)
@Mod.EventBusSubscriber(modid=HostileBoats.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class HostileBoatsForge {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HostileBoats.MOD_ID);
    public static final RegistryObject<EntityType<HostileBoatEntity>> BOAT_ENTITY_TYPE = ENTITY_TYPES.register("hostile_boat", () -> HostileBoats.BOAT_ENTITY_BUILDER.build("hostile_boats"));

    public HostileBoatsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(HostileBoats.MOD_ID, eventBus);
        eventBus.register(new HostileBoatsForgeClient());
        eventBus.register(this);
        ENTITY_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(BOAT_ENTITY_TYPE.get(), HostileBoats.createBoatAttributes().build());
    }
}

package com.github.shiftjis.hostileboats.forge;

import com.github.shiftjis.hostileboats.renderer.HostileBoatEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HostileBoatsForgeClient {
    @SubscribeEvent
    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(HostileBoatsForge.BOAT_ENTITY_TYPE.get(), HostileBoatEntityRenderer::new);
    }
}

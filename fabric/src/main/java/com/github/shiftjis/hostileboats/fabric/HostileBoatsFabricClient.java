package com.github.shiftjis.hostileboats.fabric;

import com.github.shiftjis.hostileboats.render.HostileBoatEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class HostileBoatsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(HostileBoatsFabric.BOAT_ENTITY_TYPE, HostileBoatEntityRenderer::new);
    }
}

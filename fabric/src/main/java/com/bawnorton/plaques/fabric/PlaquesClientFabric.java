package com.bawnorton.plaques.fabric;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntityTypes;
import com.bawnorton.plaques.client.PlaquesClient;
import com.bawnorton.plaques.client.render.PlaqueBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class PlaquesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlaquesClient.init();
        BlockEntityRendererRegistry.register(PlaqueBlockEntityTypes.PLAQUE, PlaqueBlockEntityRenderer::new);
    }
}

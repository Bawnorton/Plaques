package com.bawnorton.plaques.fabric;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.block.entity.PlaqueBlockEntityTypes;
import com.bawnorton.plaques.client.render.PlaqueBlockEntityRenderer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class PlaquesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Plaques.init();
        BlockEntityRendererRegistry.register(PlaqueBlockEntityTypes.PLAQUE, PlaqueBlockEntityRenderer::new);
    }
}

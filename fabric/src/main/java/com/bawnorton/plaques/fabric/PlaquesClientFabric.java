package com.bawnorton.plaques.fabric;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.client.PlaquesClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class PlaquesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlaquesClient.init();
        BlockRenderLayerMap.INSTANCE.putBlock(Plaques.ANDESITE_PLAQUE.get(), RenderLayer.getCutout());
    }
}

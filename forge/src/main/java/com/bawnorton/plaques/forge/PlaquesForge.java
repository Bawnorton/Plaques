package com.bawnorton.plaques.forge;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntityTypes;
import com.bawnorton.plaques.client.PlaquesClient;
import com.bawnorton.plaques.client.render.PlaqueBlockEntityRenderer;
import dev.architectury.platform.forge.EventBuses;
import com.bawnorton.plaques.Plaques;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Plaques.MOD_ID)
public class PlaquesForge {
    public PlaquesForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Plaques.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Plaques.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Plaques.MOD_ID, value = Dist.CLIENT)
    public static class Client {

        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            PlaquesClient.init();
        }

        @SubscribeEvent
        public static void onRegisterBlockEntityRendererEvent(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(PlaqueBlockEntityTypes.PLAQUE, PlaqueBlockEntityRenderer::new);
        }
    }
}

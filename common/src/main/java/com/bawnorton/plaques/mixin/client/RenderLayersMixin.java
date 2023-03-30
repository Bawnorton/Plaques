package com.bawnorton.plaques.mixin.client;

import com.bawnorton.plaques.client.render.NormalRenderLayer;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLayer.class)
public abstract class RenderLayersMixin {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onInit(CallbackInfo ci) {
        NormalRenderLayer.init();
    }
}

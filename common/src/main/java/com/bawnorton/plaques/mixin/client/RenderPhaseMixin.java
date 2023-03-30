package com.bawnorton.plaques.mixin.client;

import com.bawnorton.plaques.client.render.NormalShaderProgram;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPhase.class)
public abstract class RenderPhaseMixin {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onInit(CallbackInfo ci) {
        NormalShaderProgram.init();
    }
}

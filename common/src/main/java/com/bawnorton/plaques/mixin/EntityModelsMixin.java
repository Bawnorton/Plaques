package com.bawnorton.plaques.mixin;

import com.bawnorton.plaques.client.render.PlaqueBlockEntityRenderer;
import com.bawnorton.plaques.client.render.model.PlaqueEntityModelLayers;
import com.bawnorton.plaques.util.PlaqueType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.Map;

@Mixin(EntityModels.class)
public class EntityModelsMixin {
    @Inject(method = "getModels", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void addPlaques(CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> cir, ImmutableMap.Builder builder, TexturedModelData texturedModelData, TexturedModelData texturedModelData2, TexturedModelData texturedModelData3, TexturedModelData texturedModelData4, TexturedModelData texturedModelData5, TexturedModelData texturedModelData6, TexturedModelData texturedModelData7, TexturedModelData texturedModelData8, TexturedModelData texturedModelData9, TexturedModelData texturedModelData10, TexturedModelData texturedModelData11, TexturedModelData texturedModelData12, TexturedModelData texturedModelData13, TexturedModelData texturedModelData14, TexturedModelData texturedModelData15, TexturedModelData texturedModelData16, TexturedModelData texturedModelData17, TexturedModelData texturedModelData18, TexturedModelData texturedModelData19, TexturedModelData texturedModelData20, TexturedModelData texturedModelData21) {
        TexturedModelData plaque = PlaqueBlockEntityRenderer.getTextureModelData();
        Arrays.stream(PlaqueType.values()).forEach(type -> {
            builder.put(PlaqueEntityModelLayers.createPlaque(type), plaque);
        });
    }
}

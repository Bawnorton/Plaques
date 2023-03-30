package com.bawnorton.plaques.mixin.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("unused")
@Mixin(RenderLayer.class)
public interface RenderLayerInvoker {
    @Mixin(RenderLayer.MultiPhase.class)
    interface MultiPhaseInvoker {
        @Invoker("<init>")
        static RenderLayer.MultiPhase create(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, RenderLayer.MultiPhaseParameters phases) {
            throw new AssertionError();
        }
    }
}

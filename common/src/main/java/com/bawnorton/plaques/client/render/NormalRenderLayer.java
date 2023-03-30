package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.mixin.client.RenderLayerInvoker;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public abstract class NormalRenderLayer {
    private static final Function<Identifier, RenderLayer> LAYER;

    public static RenderLayer get(Identifier texture) {
        return LAYER.apply(texture);
    }

    public static RenderLayer.MultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, RenderLayer.MultiPhaseParameters phases) {
        return RenderLayerInvoker.MultiPhaseInvoker.create(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, phases);
    }

    static {
        LAYER = Util.memoize((texture) -> of("plaque_text", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder().program(RenderPhase.POSITION_TEXTURE_PROGRAM).texture(new RenderPhase.Texture((Identifier) texture, false, false)).transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY).lightmap(RenderPhase.ENABLE_LIGHTMAP).build(false)));
    }

    public static void init() {
        Plaques.LOGGER.debug("Initialized NormalRenderLayer");
    }
}

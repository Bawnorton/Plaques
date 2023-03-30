package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.Plaques;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderPhase;

public abstract class NormalShaderProgram {
    public static final RenderPhase.ShaderProgram PROGRAM;
    
    static {
        PROGRAM = new RenderPhase.ShaderProgram(GameRenderer::getPositionTexColorNormalProgram);
    }

    public static void init() {
        Plaques.LOGGER.debug("Initialized NormalShaderProgram");
    }
}

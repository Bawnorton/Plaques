package com.bawnorton.plaques.client.render;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.function.Function;

public class EngravedTextRenderer extends TextRenderer {
    public EngravedTextRenderer(Function<Identifier, FontStorage> fontStorageAccessor, boolean validateAdvance) {
        super(fontStorageAccessor, validateAdvance);
    }
    public void drawEngravedText(OrderedText text, float x, float y, int colour, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType type, int backgroundColor, int light) {
        colour = tweakTransparency(colour);
        Matrix4f matrix4f = new Matrix4f(matrix);
        this.drawLayer(text, x, y, colour, false, matrix4f, vertexConsumers, type, backgroundColor, light);
    }

    private static int tweakTransparency(int argb) {
        return (((argb & -67108864) == 0 ? argb | -16777216 : argb) & 0x00FFFFFF) | (0x80 << 24);
    }
}

package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.util.ColourHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.font.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class PlaqueTextRenderer extends TextRenderer {
    public PlaqueTextRenderer(Function<Identifier, FontStorage> fontStorageAccessor, boolean validateAdvance) {
        super(fontStorageAccessor, validateAdvance);
    }

    public void drawEngravedText(OrderedText text, float x, float y, int colour, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType type, int backgroundColor, int light) {
        colour = (tweakTransparency(colour) & 0x00FFFFFF) | (0x80 << 24);
        Matrix4f matrix4f = new Matrix4f(matrix);
        this.drawLayer(text, x, y, colour, false, matrix4f, vertexConsumers, type, backgroundColor, light);
    }

    public void drawVariatingText(OrderedText text, float x, float y, int color, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light) {
        color = tweakTransparency(color);
        Matrix4f matrix4f = new Matrix4f(matrix);
        this.drawVariatingLayer(text, x, y, color, false, matrix4f, vertexConsumers, layerType, backgroundColor, light);
    }

    public void drawGlow(OrderedText orderedText, float x, float y, float offset, int colour, Matrix4f positionMatrix, VertexConsumerProvider verticesProvider, int light) {
        positionMatrix.translate(0, 0, -0.15f);
        draw(orderedText, x + offset, y + offset, colour, false, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
        draw(orderedText, x - offset, y - offset, colour, false, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
        draw(orderedText, x + offset, y - offset, colour, false, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
        draw(orderedText, x - offset, y + offset, colour, false, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
        positionMatrix.translate(0, 0, 0.15f);
    }

    private void drawVariatingLayer(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextLayerType layerType, int underlineColor, int light) {
        Drawer drawer = new Drawer(vertexConsumerProvider, x, y, color, shadow, matrix, layerType, light, true);
        text.accept(drawer);
        drawer.drawLayer(underlineColor, x);
    }

    private static int tweakTransparency(int argb) {
        return ((argb & -67108864) == 0 ? argb | -16777216 : argb);
    }

    private class Drawer implements CharacterVisitor {
        final VertexConsumerProvider vertexConsumers;
        private final boolean shadow;
        private final float brightnessMultiplier;
        private final float red;
        private final float green;
        private final float blue;
        private float darkRed;
        private float darkGreen;
        private float darkBlue;
        private float lightRed;
        private float lightGreen;
        private float lightBlue;
        private final float alpha;
        private final Matrix4f matrix;
        private final TextLayerType layerType;
        private final int light;
        private final boolean variates;
        float x;
        float y;
        @Nullable
        private List<GlyphRenderer.Rectangle> rectangles;

        private void addRectangle(GlyphRenderer.Rectangle rectangle) {
            if (this.rectangles == null) {
                this.rectangles = Lists.newArrayList();
            }

            this.rectangles.add(rectangle);
        }

        public Drawer(VertexConsumerProvider vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, TextLayerType layerType, int light, boolean variates) {
            this.vertexConsumers = vertexConsumers;
            this.x = x;
            this.y = y;
            this.shadow = shadow;
            this.brightnessMultiplier = shadow ? 0.25F : 1.0F;
            this.red = (float)(color >> 16 & 255) / 255.0F * this.brightnessMultiplier;
            this.green = (float)(color >> 8 & 255) / 255.0F * this.brightnessMultiplier;
            this.blue = (float)(color & 255) / 255.0F * this.brightnessMultiplier;
            this.alpha = (float)(color >> 24 & 255) / 255.0F;
            this.matrix = matrix;
            this.layerType = layerType;
            this.light = light;
            this.variates = variates;

            if (variates) {
                this.darkRed = ColourHelper.darken(this.red, 0.05F);
                this.darkGreen = ColourHelper.darken(this.green, 0.05F);
                this.darkBlue = ColourHelper.darken(this.blue, 0.05F);
                this.lightRed = ColourHelper.lighten(this.red, 0.05F);
                this.lightGreen = ColourHelper.lighten(this.green, 0.05F);
                this.lightBlue = ColourHelper.lighten(this.blue, 0.05F);
            }
        }

        public boolean accept(int i, Style style, int j) {
            FontStorage fontStorage = PlaqueTextRenderer.this.getFontStorage(style.getFont());
            Glyph glyph = fontStorage.getGlyph(j, PlaqueTextRenderer.this.validateAdvance);
            GlyphRenderer glyphRenderer = style.isObfuscated() && j != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(j);
            boolean bl = style.isBold();
            float f = this.alpha;
            TextColor textColor = style.getColor();
            float r;
            float g;
            float b;

            if (textColor != null) {
                int k = textColor.getRgb();
                r = (float)(k >> 16 & 255) / 255.0F * this.brightnessMultiplier;
                g = (float)(k >> 8 & 255) / 255.0F * this.brightnessMultiplier;
                b = (float)(k & 255) / 255.0F * this.brightnessMultiplier;
            } else if (variates) {
                Random random = new Random(i * j * 312871L);
                int selection = random.nextInt(3);

                boolean monochrome = Math.abs(this.red - this.green) < 0.05 && Math.abs(this.red - this.blue) < 0.05;
                if (monochrome) {
                    boolean dark = random.nextBoolean();
                    r = dark ? this.darkRed : this.lightRed;
                    g = dark ? this.darkGreen : this.lightGreen;
                    b = dark ? this.darkBlue : this.lightBlue;
                } else if (selection <= 1) {
                    r = random.nextBoolean() ? this.darkRed : this.lightRed;
                    g = random.nextBoolean() ? this.darkGreen : this.lightGreen;
                    b = random.nextBoolean() ? this.darkBlue : this.lightBlue;
                } else {
                    r = this.red;
                    g = this.green;
                    b = this.blue;
                }
            } else {
                r = this.red;
                g = this.green;
                b = this.blue;
            }


            float n;
            float m;
            if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
                m = bl ? glyph.getBoldOffset() : 0.0F;
                n = this.shadow ? glyph.getShadowOffset() : 0.0F;
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                PlaqueTextRenderer.this.drawGlyph(glyphRenderer, bl, style.isItalic(), m, this.x + n, this.y + n, this.matrix, vertexConsumer, r, g, b, f, this.light);
            }

            m = glyph.getAdvance(bl);
            n = this.shadow ? 1.0F : 0.0F;
            if (style.isStrikethrough()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0F, this.y + n + 4.5F, this.x + n + m, this.y + n + 4.5F - 1.0F, 0.01F, r, g, b, f));
            }

            if (style.isUnderlined()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0F, this.y + n + 9.0F, this.x + n + m, this.y + n + 9.0F - 1.0F, 0.01F, r, g, b, f));
            }

            this.x += m;
            return true;
        }

        public void drawLayer(int underlineColor, float x) {
            if (underlineColor != 0) {
                float f = (float)(underlineColor >> 24 & 255) / 255.0F;
                float g = (float)(underlineColor >> 16 & 255) / 255.0F;
                float h = (float)(underlineColor >> 8 & 255) / 255.0F;
                float i = (float)(underlineColor & 255) / 255.0F;
                this.addRectangle(new GlyphRenderer.Rectangle(x - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, g, h, i, f));
            }

            if (this.rectangles != null) {
                GlyphRenderer glyphRenderer = PlaqueTextRenderer.this.getFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));

                for (GlyphRenderer.Rectangle rectangle : this.rectangles) {
                    glyphRenderer.drawRectangle(rectangle, this.matrix, vertexConsumer, this.light);
                }
            }
        }
    }
}

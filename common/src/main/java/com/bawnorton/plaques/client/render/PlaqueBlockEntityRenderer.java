package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.client.PlaquesClient;
import com.bawnorton.plaques.util.ColourHelper;
import com.bawnorton.plaques.util.PlaqueAccents;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.List;

public class PlaqueBlockEntityRenderer implements BlockEntityRenderer<PlaqueBlockEntity> {
    private static final int RENDER_DISTANCE = MathHelper.square(16);
    private final TextRenderer textRenderer;

    public PlaqueBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.textRenderer = context.getTextRenderer();
    }

    @Override
    public void render(PlaqueBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();

        matrices.push();
        matrices.translate(0.5F, 0.5F, 0.5F);
        float rotation = -state.get(WallSignBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        matrices.translate(0.0F, -0.3125F, -0.4200F);

        renderText(entity, matrices, vertexConsumers, light);
        matrices.pop();
    }

    private void renderText(PlaqueBlockEntity blockEntity, MatrixStack matrices, VertexConsumerProvider verticesProvider, int light) {
        if(blockEntity.getWorld() == null) return;

        float scale = 0.015625F * (float) 0.666667;
        Vec3d vec3d = this.getTextOffset();
        matrices.translate(vec3d.x, vec3d.y, vec3d.z);
        matrices.scale(scale, -scale, scale);
        int lineOffset = (PlaqueBlockEntity.getLineCount() - 1) * blockEntity.getTextLineHeight() / 2;
        OrderedText[] orderedTexts = blockEntity.updatePlaque(MinecraftClient.getInstance().shouldFilterText(), (text) -> {
            List<OrderedText> list = this.textRenderer.wrapLines(text, blockEntity.getMaxTextWidth() * (0 + 1));
            return list.isEmpty() ? OrderedText.EMPTY : list.get(0);
        });

        int textColour = blockEntity.getTextColour().getColour();
        int engravingColour = ColourHelper.darken(blockEntity.getPlaqueType().getColourMap(), blockEntity.isGlowingText() ? 0.5f : 0.85f);
        boolean shouldEngrave = blockEntity.getTextColour().equals(PlaqueAccents.NONE);

        boolean shouldGlow;
        if (blockEntity.isGlowingText()) {
            shouldGlow = shouldGlow(blockEntity, textColour);
        } else {
            shouldGlow = false;
        }

        int xOffset = (blockEntity.getMaxTextWidth() / 2 + 5) * 0;

        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        positionMatrix.scale(1.5f, 1.5f, 1f);
        positionMatrix.translate(0, -2.7f, 0);
        for(int line = 0; line < PlaqueBlockEntity.getLineCount(); ++line) {
            OrderedText orderedText = orderedTexts[line];
            float textWidth = (float)(-this.textRenderer.getWidth(orderedText) / 2) + xOffset;
            if (shouldGlow && shouldEngrave) {
                this.textRenderer.draw(orderedText, textWidth, (float)(line * blockEntity.getTextLineHeight() - lineOffset), engravingColour, false, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
                PlaquesClient.plaqueTextRenderer.drawGlow(orderedText, textWidth, line * blockEntity.getTextLineHeight() - lineOffset, 0.3f, ColourHelper.lighten(engravingColour, 0.2), positionMatrix, verticesProvider, light);
            } else if (shouldGlow) {
                PlaquesClient.plaqueTextRenderer.drawVariatingText(orderedText, textWidth, line * blockEntity.getTextLineHeight() - lineOffset, textColour, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
                PlaquesClient.plaqueTextRenderer.drawGlow(orderedText, textWidth, line * blockEntity.getTextLineHeight() - lineOffset, 0.3f, ColourHelper.lighten(textColour, 0.2), positionMatrix, verticesProvider, light);
            } else if (shouldEngrave) {
                PlaquesClient.plaqueTextRenderer.drawEngravedText(orderedText, textWidth, line * blockEntity.getTextLineHeight() - lineOffset, engravingColour, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
            } else {
                PlaquesClient.plaqueTextRenderer.drawVariatingText(orderedText, textWidth, (float)(line * blockEntity.getTextLineHeight() - lineOffset), textColour, positionMatrix, verticesProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
            }
        }
    }

    private Vec3d getTextOffset() {
        return new Vec3d(0.0, 0.5F * (float) 0.666667, 0.07F * (float) 0.666667);
    }

    private static boolean shouldGlow(PlaqueBlockEntity plaqueEntity, int plaqueColour) {
        if (plaqueColour == PlaqueAccents.NONE.getColour()) {
            return true;
        } else {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
            if (clientPlayerEntity != null && minecraftClient.options.getPerspective().isFirstPerson() && clientPlayerEntity.isUsingSpyglass()) {
                return true;
            } else {
                Entity entity = minecraftClient.getCameraEntity();
                return entity != null && entity.squaredDistanceTo(Vec3d.ofCenter(plaqueEntity.getPos())) < (double)RENDER_DISTANCE;
            }
        }
    }

    private static int getColour(PlaqueBlockEntity entity) {
        int[] colourArray = entity.getTextColour().getColourArray();
        return ColourHelper.darken(ColorHelper.Argb.getArgb(0, colourArray[0], colourArray[1], colourArray[2]), 0.4);
    }
}

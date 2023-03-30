package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.client.PlaquesClient;
import com.bawnorton.plaques.util.ColourHelper;
import com.bawnorton.plaques.util.PlaqueColour;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.OrderedText;
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
        int engravingColour;
        if(blockEntity.getTextColour().equals(PlaqueColour.NONE)) {
            engravingColour = ColourHelper.darken(blockEntity.getPlaqueType().getColourMap(), blockEntity.isGlowingText() ? 0.5f : 0.85f);
        } else {
            engravingColour = textColour;
        }

        boolean shouldGlow;
        int plaqueLight;
        if (blockEntity.isGlowingText()) {
            shouldGlow = shouldGlow(blockEntity, textColour);
            plaqueLight = 15728880;
        } else {
            shouldGlow = false;
            plaqueLight = light;
        }

        int xOffset = (blockEntity.getMaxTextWidth() / 2 + 5) * 0;

        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        positionMatrix.scale(1.5f, 1.5f, 1f);
        positionMatrix.translate(0, -2.7f, 0);
        for(int line = 0; line < PlaqueBlockEntity.getLineCount(); ++line) {
            OrderedText orderedText = orderedTexts[line];
            float textWidth = (float)(-this.textRenderer.getWidth(orderedText) / 2) + xOffset;
            if (shouldGlow) {
                this.textRenderer.draw(orderedText, textWidth, (float)(line * blockEntity.getTextLineHeight() - lineOffset), engravingColour, false, positionMatrix, verticesProvider, false, 0, plaqueLight);
                // glow effect
                float offset = 0.2f;
                positionMatrix.translate(0, 0, -0.05f);
                this.textRenderer.draw(orderedText, textWidth + offset, (float)(line * blockEntity.getTextLineHeight() - lineOffset) + offset, 0xFFEECC, false, positionMatrix, verticesProvider, false, 0, plaqueLight);
                this.textRenderer.draw(orderedText, textWidth - offset, (float)(line * blockEntity.getTextLineHeight() - lineOffset) - offset, 0xFFEECC, false, positionMatrix, verticesProvider, false, 0, plaqueLight);
                this.textRenderer.draw(orderedText, textWidth + offset, (float)(line * blockEntity.getTextLineHeight() - lineOffset) - offset, 0xFFEECC, false, positionMatrix, verticesProvider, false, 0, plaqueLight);
                this.textRenderer.draw(orderedText, textWidth - offset, (float)(line * blockEntity.getTextLineHeight() - lineOffset) + offset, 0xFFEECC, false, positionMatrix, verticesProvider, false, 0, plaqueLight);
                positionMatrix.translate(0, 0, 0.05f);
            } else {
                PlaquesClient.engravedTextRenderer.drawEngravedText(orderedText, textWidth, line * blockEntity.getTextLineHeight() - lineOffset, engravingColour, positionMatrix, verticesProvider, false, 0, plaqueLight);
            }
        }
    }

    private Vec3d getTextOffset() {
        return new Vec3d(0.0, 0.5F * (float) 0.666667, 0.07F * (float) 0.666667);
    }

    private static boolean shouldGlow(PlaqueBlockEntity plaqueEntity, int plaqueColour) {
        if (plaqueColour == PlaqueColour.NONE.getColour()) {
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
        return ColourHelper.darken(NativeImage.packColor(0, colourArray[2], colourArray[1], colourArray[0]), 0.4);
    }
}

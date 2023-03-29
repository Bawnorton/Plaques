package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.client.PlaquesClient;
import com.bawnorton.plaques.util.ColourHelper;
import com.bawnorton.plaques.util.PlaqueColour;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
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

    private int resolveRightPlaques(PlaqueBlockEntity leftMost, PlaqueBlockEntity rightPlaque, Direction right, int depth) {
        if(rightPlaque.getPlaqueType().equals(leftMost.getPlaqueType())) {
            boolean filter = MinecraftClient.getInstance().shouldFilterText();
            String[] leftText = leftMost.getText(filter);
            String[] rightText = rightPlaque.getText(filter);
            String[] newText = new String[leftText.length];
            for(int i = 0; i < leftText.length; i++) {
                newText[i] = leftText[i] + rightText[i];
            }
            leftMost.setText(newText);
            rightPlaque.clearText();
            BlockEntity nextRight = leftMost.getWorld().getBlockEntity(rightPlaque.getPos().offset(right));
            if(nextRight instanceof PlaqueBlockEntity nextRightPlaque) {
                return resolveRightPlaques(leftMost, nextRightPlaque, right,depth + 1);
            }
        }
        return depth;
    }

    private void renderText(PlaqueBlockEntity blockEntity, MatrixStack matrices, VertexConsumerProvider verticesProvider, int light) {
        if(blockEntity.getWorld() == null) return;

        BlockPos blockPos = blockEntity.getPos();
        Direction direction = blockEntity.getCachedState().get(WallSignBlock.FACING);
        Direction left = direction.rotateYCounterclockwise();
        BlockEntity leftBlock = blockEntity.getWorld().getBlockEntity(blockPos.offset(left));
        if(leftBlock instanceof PlaqueBlockEntity leftPlaqueBlockEntity) {
            if (blockEntity.getPlaqueType().equals(leftPlaqueBlockEntity.getPlaqueType())) {
                return;
            }
        }

        Direction right = direction.rotateYClockwise();
        BlockEntity rightBlock = blockEntity.getWorld().getBlockEntity(blockPos.offset(right));
        int numRightPlaques = 0;
        if(rightBlock instanceof PlaqueBlockEntity rightPlaqueBlockEntity) {
            numRightPlaques = resolveRightPlaques(blockEntity, rightPlaqueBlockEntity, right, 1);
        }

        int xOffset = (- blockEntity.getMaxTextWidth() / 2 - 5) * numRightPlaques;

        float scale = 0.015625F * (float) 0.666667;
        Vec3d vec3d = this.getTextOffset();
        matrices.translate(vec3d.x, vec3d.y, vec3d.z);
        matrices.scale(scale, -scale, scale);
        int lineOffset = (PlaqueBlockEntity.getLineCount() - 1) * blockEntity.getTextLineHeight() / 2;
        OrderedText[] orderedTexts = blockEntity.updatePlaque(MinecraftClient.getInstance().shouldFilterText(), (text) -> {
            List<OrderedText> list = this.textRenderer.wrapLines(text, blockEntity.getMaxTextWidth());
            return list.isEmpty() ? OrderedText.EMPTY : list.get(0);
        });

        int textColour = blockEntity.getTextColor().getColour();
        int engravingColour;
        if(blockEntity.getTextColor().equals(PlaqueColour.NONE)) {
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


        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        positionMatrix.scale(1.5f, 1.5f, 1f);
        positionMatrix.translate(0, -3f, 0);
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
        int[] colourArray = entity.getTextColor().getColourArray();
        return ColourHelper.darken(NativeImage.packColor(0, colourArray[2], colourArray[1], colourArray[0]), 0.4);
    }
}

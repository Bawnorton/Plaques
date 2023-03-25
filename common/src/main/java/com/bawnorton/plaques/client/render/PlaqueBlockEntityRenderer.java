package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
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
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

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
        float scale = 0.015625F * (float) 0.666667;
        Vec3d vec3d = this.getTextOffset();
        matrices.translate(vec3d.x, vec3d.y, vec3d.z);
        matrices.scale(scale, -scale, scale);
        int colour = getColour(blockEntity);
        int lineOffset = (PlaqueBlockEntity.getLineCount() - 1) * blockEntity.getTextLineHeight() / 2;
        OrderedText[] orderedTexts = blockEntity.updateSign(MinecraftClient.getInstance().shouldFilterText(), (text) -> {
            List<OrderedText> list = this.textRenderer.wrapLines(text, blockEntity.getMaxTextWidth());
            return list.isEmpty() ? OrderedText.EMPTY : list.get(0);
        });
        int textColour;
        boolean shouldRender;
        int plaqueLight;
        if (blockEntity.isGlowingText()) {
            textColour = blockEntity.getTextColor().getSignColor();
            shouldRender = shouldRender(blockEntity, textColour);
            plaqueLight = 15728880;
        } else {
            textColour = colour;
            shouldRender = false;
            plaqueLight = light;
        }

        for(int line = 0; line < PlaqueBlockEntity.getLineCount(); ++line) {
            OrderedText orderedText = orderedTexts[line];
            float textWidth = (float)(-this.textRenderer.getWidth(orderedText) / 2);
            if (shouldRender) {
                this.textRenderer.drawWithOutline(orderedText, textWidth, (float)(line * blockEntity.getTextLineHeight() - lineOffset), textColour, colour, matrices.peek().getPositionMatrix(), verticesProvider, plaqueLight);
            } else {
                this.textRenderer.draw(orderedText, textWidth, (float)(line * blockEntity.getTextLineHeight() - lineOffset), textColour, false, matrices.peek().getPositionMatrix(), verticesProvider, false, 0, plaqueLight);
            }
        }
    }

    private Vec3d getTextOffset() {
        return new Vec3d(0.0, 0.5F * (float) 0.666667, 0.07F * (float) 0.666667);
    }

    private static boolean shouldRender(PlaqueBlockEntity plaqueEntity, int plaqueColour) {
        if (plaqueColour == DyeColor.BLACK.getSignColor()) {
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
        int colour = entity.getTextColor().getSignColor();
        int red = (int) (NativeImage.getRed(colour) * 0.4);
        int green = (int) (NativeImage.getGreen(colour) * 0.4);
        int blue = (int) (NativeImage.getBlue(colour) * 0.4);
        return NativeImage.packColor(0, blue, green, red);
    }
}

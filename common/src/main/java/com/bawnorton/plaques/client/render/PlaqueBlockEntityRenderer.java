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
    public static final int MAX_TEXT_WIDTH = 90;
    private static final int TEXT_HEIGHT = 10;
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
        float h = -state.get(WallSignBlock.FACING).getOpposite().asRotation();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
        matrices.translate(0.0F, -0.3125F, -0.4375F);

        renderText(entity, matrices, vertexConsumers, light, 0.666667f);
    }

    private void renderText(PlaqueBlockEntity blockEntity, MatrixStack matrices, VertexConsumerProvider verticesProvider, int light, float scale) {
        float f = 0.015625F * scale;
        Vec3d vec3d = this.getTextOffset(scale);
        matrices.translate(vec3d.x, vec3d.y, vec3d.z);
        matrices.scale(f, -f, f);
        int i = getColour(blockEntity);
        int j = 4 * blockEntity.getTextLineHeight() / 2;
        OrderedText[] orderedTexts = blockEntity.updateSign(MinecraftClient.getInstance().shouldFilterText(), (text) -> {
            List<OrderedText> list = this.textRenderer.wrapLines(text, blockEntity.getMaxTextWidth());
            return list.isEmpty() ? OrderedText.EMPTY : list.get(0);
        });
        int k;
        boolean bl;
        int l;
        if (blockEntity.isGlowingText()) {
            k = blockEntity.getTextColor().getSignColor();
            bl = shouldRender(blockEntity, k);
            l = 15728880;
        } else {
            k = i;
            bl = false;
            l = light;
        }

        for(int m = 0; m < 4; ++m) {
            OrderedText orderedText = orderedTexts[m];
            float g = (float)(-this.textRenderer.getWidth(orderedText) / 2);
            if (bl) {
                this.textRenderer.drawWithOutline(orderedText, g, (float)(m * blockEntity.getTextLineHeight() - j), k, i, matrices.peek().getPositionMatrix(), verticesProvider, l);
            } else {
                this.textRenderer.draw(orderedText, g, (float)(m * blockEntity.getTextLineHeight() - j), k, false, matrices.peek().getPositionMatrix(), verticesProvider, false, 0, l);
            }
        }

        matrices.pop();
    }

    Vec3d getTextOffset(float scale) {
        return new Vec3d(0.0, 0.5F * scale, 0.07F * scale);
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

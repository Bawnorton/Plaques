package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.block.PlaqueBlock;
import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.client.render.model.PlaqueEntityModelLayers;
import com.bawnorton.plaques.util.PlaqueType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.OrderedText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlaqueBlockEntityRenderer implements BlockEntityRenderer<PlaqueBlockEntity> {
    public static final int MAX_TEXT_WIDTH = 90;
    private static final int TEXT_HEIGHT = 10;
    private static final int RENDER_DISTANCE = MathHelper.square(16);
    private final Map<PlaqueType, PlaqueModel> typeToModel;
    private final TextRenderer textRenderer;

    public PlaqueBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.typeToModel = Arrays.stream(PlaqueType.values()).collect(ImmutableMap.toImmutableMap(type -> type, type -> new PlaqueModel(context.getLayerModelPart(PlaqueEntityModelLayers.createPlaque(type)))));
        this.textRenderer = context.getTextRenderer();
    }

    public static TexturedModelData getTextureModelData() {
        ModelData modelData = new ModelData();
        ModelPartData partData = modelData.getRoot();
        partData.addChild("plaque", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render(PlaqueBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();

        matrices.push();
        PlaqueType type = PlaqueType.fromBlock(state.getBlock());
        PlaqueModel model = this.typeToModel.get(type);
        if(model == null) {
            throw new IllegalStateException("No model for plaque type " + type);
        }

        matrices.translate(0.5, 0.5, 0.5);
        float rotation = -state.get(PlaqueBlock.FACING).asRotation();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
        matrices.translate(0.0, -0.3125, -0.4375);

        matrices.push();
        matrices.scale(0.6666667F, -0.6666667F, -0.6666667F);
        SpriteIdentifier spriteIdentifier = PlaqueTexturedRenderLayers.getPlaqueTextureId(type);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, model::getLayer);
        model.root.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
        matrices.translate(0.0, 0.33333334F, 0.046666667F);
        matrices.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int textColor = getColour(entity);

        OrderedText[] orderedTexts = entity.updateSign(MinecraftClient.getInstance().shouldFilterText(), text -> {
            List<OrderedText> list = this.textRenderer.wrapLines(text, MAX_TEXT_WIDTH);
            return list.isEmpty() ? OrderedText.EMPTY : list.get(0);
        });

        int colour;
        boolean shouldRenderOutline;
        int plaqueLight;
        if(entity.isGlowingText()) {
            colour = entity.getTextColor().getSignColor();
            shouldRenderOutline = shouldRender(entity, colour);
            plaqueLight = 15728880;
        } else {
            colour = textColor;
            shouldRenderOutline = false;
            plaqueLight = light;
        }

        for(int i = 0; i < 4; ++i) {
            OrderedText orderedText = orderedTexts[i];
            float width = (float)(-this.textRenderer.getWidth(orderedText) / 2);
            if(shouldRenderOutline) {
                this.textRenderer.drawWithOutline(orderedText, width, (float)(i * TEXT_HEIGHT - 20), colour, textColor, matrices.peek().getPositionMatrix(), vertexConsumers, plaqueLight);
            } else {
                this.textRenderer.draw(orderedText, width, (float)(i * TEXT_HEIGHT - 20), colour, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0, plaqueLight);
            }
        }

        matrices.pop();
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

    public static final class PlaqueModel extends Model {
        public final ModelPart root;

        public PlaqueModel(ModelPart root) {
            super(RenderLayer::getEntityCutoutNoCull);
            this.root = root;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }
}

package com.bawnorton.plaques.client.screen;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.*;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.stream.IntStream;

@SuppressWarnings("ConstantConditions")
public class PlaqueScreen extends Screen {
    private final PlaqueBlockEntity plaqueEntity;
    private final String[] text;
    private int ticksSinceOpened;
    private int currentRow;
    private SelectionManager selectionManager;

    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);

    public PlaqueScreen(PlaqueBlockEntity plaqueEntity, boolean filtered) {
        super(Text.translatable("plaques.edit"));
        this.plaqueEntity = plaqueEntity;
        this.text = IntStream.range(0, 4).mapToObj(row -> plaqueEntity.getTextOnRow(row, filtered)).map(Text::getString).toArray(String[]::new);

        super.init(MinecraftClient.getInstance(), MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight());
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.finishEditing();
        }).dimensions(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build());
        this.plaqueEntity.setEditable(false);
        this.selectionManager = new SelectionManager(() -> this.text[this.currentRow], (rowText) -> {
            this.text[this.currentRow] = rowText;
            this.plaqueEntity.setTextOnRow(this.currentRow, Text.literal(rowText));
        }, SelectionManager.makeClipboardGetter(this.client), SelectionManager.makeClipboardSetter(this.client), (string) -> this.client.textRenderer.getWidth(string) <= this.plaqueEntity.getMaxTextWidth());
    }

    public void removed() {
        ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
        if (clientPlayNetworkHandler != null) {
            clientPlayNetworkHandler.sendPacket(new UpdateSignC2SPacket(this.plaqueEntity.getPos(), this.text[0], this.text[1], this.text[2], this.text[3]));
        }

        this.plaqueEntity.setEditable(true);
    }

    public void tick() {
        ++this.ticksSinceOpened;
        if (!this.plaqueEntity.getType().supports(this.plaqueEntity.getCachedState())) {
            this.finishEditing();
        }
    }

    private void finishEditing() {
        this.plaqueEntity.markDirty();
        this.client.setScreen(null);
    }

    public boolean charTyped(char chr, int modifiers) {
        this.selectionManager.insert(chr);
        return true;
    }

    public void close() {
        this.finishEditing();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 265) {
            this.currentRow = this.currentRow - 1 & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        } else if (keyCode != 264 && keyCode != 257 && keyCode != 335) {
            return this.selectionManager.handleSpecialKey(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            this.currentRow = this.currentRow + 1 & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 40, 16777215);
        this.renderPlaque(matrices);
        DiffuseLighting.enableGuiDepthLighting();
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void renderPlaqueBackground(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        // TODO: Implement this
    }

    private Vector3f getTextScale() {
        return TEXT_SCALE;
    }

    protected void translateForRender(MatrixStack matrices) {
        matrices.translate((float)this.width / 2.0F, 90.0F, 50.0F);
    }

    private void renderPlaque(MatrixStack matrices) {
        VertexConsumerProvider.Immediate immediate = this.client.getBufferBuilders().getEntityVertexConsumers();
        matrices.push();
        this.translateForRender(matrices);
        matrices.push();
        this.renderPlaqueBackground(matrices, immediate);
        matrices.pop();
        this.renderSignText(matrices, immediate);
        matrices.pop();
    }

    private void renderSignText(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        matrices.translate(0.0F, 0.0F, 4.0F);
        Vector3f vector3f = this.getTextScale();
        matrices.scale(vector3f.x(), vector3f.y(), vector3f.z());
        int i = this.plaqueEntity.getTextColor().getSignColor();
        boolean bl = this.ticksSinceOpened / 6 % 2 == 0;
        int j = this.selectionManager.getSelectionStart();
        int k = this.selectionManager.getSelectionEnd();
        int l = 4 * this.plaqueEntity.getTextLineHeight() / 2;
        int m = this.currentRow * this.plaqueEntity.getTextLineHeight() - l;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        int n;
        String string;
        int o;
        int p;
        for(n = 0; n < this.text.length; ++n) {
            string = this.text[n];
            if (string != null) {
                if (this.textRenderer.isRightToLeft()) {
                    string = this.textRenderer.mirror(string);
                }

                float f = (float)(-this.client.textRenderer.getWidth(string) / 2);
                this.client.textRenderer.draw(string, f, (float)(n * this.plaqueEntity.getTextLineHeight() - l), i, false, matrix4f, vertexConsumers, false, 0, 15728880, false);
                if (n == this.currentRow && j >= 0 && bl) {
                    o = this.client.textRenderer.getWidth(string.substring(0, Math.min(j, string.length())));
                    p = o - this.client.textRenderer.getWidth(string) / 2;
                    if (j >= string.length()) {
                        this.client.textRenderer.draw("_", (float)p, (float)m, i, false, matrix4f, vertexConsumers, false, 0, 15728880, false);
                    }
                }
            }
        }

        vertexConsumers.draw();

        for(n = 0; n < this.text.length; ++n) {
            string = this.text[n];
            if (string != null && n == this.currentRow && j >= 0) {
                int q = this.client.textRenderer.getWidth(string.substring(0, Math.min(j, string.length())));
                o = q - this.client.textRenderer.getWidth(string) / 2;
                if (bl && j < string.length()) {
                    fill(matrices, o, m - 1, o + 1, m + this.plaqueEntity.getTextLineHeight(), -16777216 | i);
                }

                if (k != j) {
                    p = Math.min(j, k);
                    int r = Math.max(j, k);
                    int s = this.client.textRenderer.getWidth(string.substring(0, p)) - this.client.textRenderer.getWidth(string) / 2;
                    int t = this.client.textRenderer.getWidth(string.substring(0, r)) - this.client.textRenderer.getWidth(string) / 2;
                    int u = Math.min(s, t);
                    int v = Math.max(s, t);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferBuilder = tessellator.getBuffer();
                    RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                    RenderSystem.disableTexture();
                    RenderSystem.enableColorLogicOp();
                    RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                    bufferBuilder.vertex(matrix4f, (float)u, (float)(m + this.plaqueEntity.getTextLineHeight()), 0.0F).color(0, 0, 255, 255).next();
                    bufferBuilder.vertex(matrix4f, (float)v, (float)(m + this.plaqueEntity.getTextLineHeight()), 0.0F).color(0, 0, 255, 255).next();
                    bufferBuilder.vertex(matrix4f, (float)v, (float)m, 0.0F).color(0, 0, 255, 255).next();
                    bufferBuilder.vertex(matrix4f, (float)u, (float)m, 0.0F).color(0, 0, 255, 255).next();
                    BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                    RenderSystem.disableColorLogicOp();
                    RenderSystem.enableTexture();
                }
            }
        }
    }
}

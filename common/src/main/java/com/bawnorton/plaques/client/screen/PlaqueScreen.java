package com.bawnorton.plaques.client.screen;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.client.networking.ClientNetworking;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@SuppressWarnings("ConstantConditions")
public class PlaqueScreen extends Screen {
    private final PlaqueBlockEntity plaqueEntity;
    private final String[] text;
    private final int numPlaques;
    private int ticksSinceOpened;
    private int currentRow;
    private SelectionManager selectionManager;

    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);

    public PlaqueScreen(PlaqueBlockEntity plaqueEntity, boolean filtered) {
        super(Text.translatable("plaques.edit"));
        this.plaqueEntity = plaqueEntity;
        this.numPlaques = 1;
        this.text = this.plaqueEntity.getText(filtered);

        client = MinecraftClient.getInstance();
        super.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.finishEditing();
        }).dimensions(this.width / 2 - 100, this.height / PlaqueBlockEntity.getLineCount() + 120, 200, 20).build());
        this.plaqueEntity.setEditable(false);
        this.selectionManager = new SelectionManager(() -> this.text[this.currentRow], (rowText) -> {
            this.text[this.currentRow] = rowText;
            this.plaqueEntity.setTextOnRow(this.currentRow, Text.literal(rowText));
        }, SelectionManager.makeClipboardGetter(this.client), SelectionManager.makeClipboardSetter(this.client), (string) -> this.client.textRenderer.getWidth(string) <= this.plaqueEntity.getMaxTextWidth() * this.numPlaques);
    }

    public void removed() {
        ClientNetworking.updatePlaque(this.plaqueEntity.getPos(), this.plaqueEntity.getText(client.shouldFilterText()));
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
            this.currentRow = this.currentRow - 1;
            if(this.currentRow < 0) this.currentRow = PlaqueBlockEntity.getLineCount() - 1;
            this.selectionManager.putCursorAtEnd();
            return true;
        } else if (keyCode != 264 && keyCode != 257 && keyCode != 335) {
            return this.selectionManager.handleSpecialKey(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            this.currentRow = this.currentRow + 1;
            if(this.currentRow >= PlaqueBlockEntity.getLineCount()) this.currentRow = 0;
            this.selectionManager.putCursorAtEnd();
            return true;
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground(matrices);
        drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 40, 16777215);
        this.renderPlaque(matrices);
        DiffuseLighting.enableGuiDepthLighting();
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void renderPlaqueBackground(MatrixStack matrices) {
        Identifier texture = plaqueEntity.getPlaqueType().getTexture();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, texture);
        Window window = MinecraftClient.getInstance().getWindow();
        int width = window.getScaledWidth();
        drawTexture(matrices, width / 2 - 32, 62, 0, 0,64, 56, 64, 56);
    }

    private Vector3f getTextScale() {
        return TEXT_SCALE;
    }

    protected void translateForRender(MatrixStack matrices) {
        matrices.translate((float)this.width / 2.0F, 90.0F, 50.0F);
    }

    private void renderPlaque(MatrixStack matrices) {
        VertexConsumerProvider.Immediate immediate = this.client.getBufferBuilders().getEntityVertexConsumers();
        this.renderPlaqueBackground(matrices);
        matrices.push();
        this.translateForRender(matrices);
        this.renderPlaqueText(matrices, immediate);
        matrices.pop();
    }

    private void renderPlaqueText(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        matrices.translate(0.0F, 0.0F, 4.0F);
        Vector3f vector3f = this.getTextScale();
        matrices.scale(vector3f.x(), vector3f.y(), vector3f.z());
        int i = this.plaqueEntity.getTextColour().getColour();
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
                this.client.textRenderer.draw(string, f, (float)(n * this.plaqueEntity.getTextLineHeight() - l), i, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880, false);
                if (n == this.currentRow && j >= 0 && bl) {
                    o = this.client.textRenderer.getWidth(string.substring(0, Math.max(Math.min(j, string.length()), 0)));
                    p = o - this.client.textRenderer.getWidth(string) / 2;
                    if (j >= string.length()) {
                        this.client.textRenderer.draw("_", (float)p, (float)m, i, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880, false);
                    }
                }
            }
        }

        vertexConsumers.draw();

        for(n = 0; n < this.text.length; ++n) {
            string = this.text[n];
            if (string != null && n == this.currentRow && j >= 0) {
                int q = this.client.textRenderer.getWidth(string.substring(0, Math.max(Math.min(j, string.length()), 0)));
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
                    RenderSystem.enableColorLogicOp();
                    RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
                    fill(matrices, u, m, v, m + this.plaqueEntity.getTextLineHeight(), -16776961);
                    RenderSystem.disableColorLogicOp();
                }
            }
        }
    }
}

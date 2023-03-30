package com.bawnorton.plaques.block.entity;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.block.PlaqueBlock;
import com.bawnorton.plaques.util.PlaqueAccents;
import com.bawnorton.plaques.util.PlaqueType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class PlaqueBlockEntity extends BlockEntity {
    private final PlaqueData data;

    public PlaqueBlockEntity(BlockPos pos, BlockState state) {
        super(Plaques.PLAQUE.get(), pos, state);
        data = new PlaqueData();
    }

    public static int getLineCount() {
        return PlaqueData.getLineCount();
    }

    public boolean setAccent(Item item) {
        if (PlaqueAccents.hasAccent(item)) {
            return this.setTextColour(PlaqueAccents.byItem(item));
        }
        return false;
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        data.writeNbt(nbt);
    }

    public void readNbt(NbtCompound nbt) {
        data.setEditable(false);
        super.readNbt(nbt);
        data.readNbt(nbt, this.world, this.pos);
    }

    public Text getTextOnRow(int row, boolean filtered) {
        return getTexts(filtered).get(row);
    }

    public void setTextOnRow(int row, Text text) {
        setTextOnRow(row, text, text);
    }

    public void setTextOnRow(int row, Text text, Text filteredText) {
        data.getTexts().set(row, text);
        data.getFilteredTexts().set(row, filteredText);
        data.setTextsBeingEdited(null);
    }

    public OrderedText[] updatePlaque(boolean filterText, Function<Text, OrderedText> textOrderingFunction) {
        if (data.getTextsBeingEdited() == null || data.isFilterText() != filterText) {
            data.setFilterText(filterText);
            data.setTextsBeingEdited(new OrderedText[getLineCount()]);

            for (int i = 0; i < getLineCount(); ++i) {
                data.getTextsBeingEdited()[i] = textOrderingFunction.apply(this.getTextOnRow(i, filterText));
            }
        }

        return data.getTextsBeingEdited();
    }

    private List<Text> getTexts(boolean filtered) {
        return filtered ? data.getFilteredTexts() : data.getTexts();
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public boolean copyItemDataRequiresOperator() {
        return true;
    }

    public boolean isEditable() {
        return data.isEditable();
    }

    public void setEditable(boolean editable) {
        data.setEditable(editable);
        if (!editable) {
            data.setEditor(null);
        }
    }

    @Nullable
    public UUID getEditor() {
        return data.getEditor();
    }

    public void setEditor(@Nullable UUID editor) {
        data.setEditor(editor);
    }

    public boolean shouldRunCommand(PlayerEntity player) {
        for (Text text : this.getTexts(player.shouldFilterText())) {
            Style style = text.getStyle();
            ClickEvent clickEvent = style.getClickEvent();
            if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                return true;
            }
        }

        return false;
    }

    public boolean onActivate(ServerPlayerEntity player) {
        for (Text text : this.getTexts(player.shouldFilterText())) {
            Style style = text.getStyle();
            ClickEvent clickEvent = style.getClickEvent();
            if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                player.getServer().getCommandManager().executeWithPrefix(this.getCommandSource(player), clickEvent.getValue());
            }
        }
        return true;
    }

    public ServerCommandSource getCommandSource(@Nullable ServerPlayerEntity player) {
        String string = player == null ? "Plaque" : player.getName().getString();
        Text text = player == null ? Text.literal("Plaque") : player.getDisplayName();
        return new ServerCommandSource(CommandOutput.DUMMY, Vec3d.ofCenter(this.pos), Vec2f.ZERO, (ServerWorld) this.world, 2, string, text, this.world.getServer(), player);
    }

    public PlaqueAccents getTextColour() {
        return data.getTextColour();
    }

    public boolean setTextColour(PlaqueAccents value) {
        boolean result = data.setTextColour(value);
        if (result) this.updateListeners();
        return result;
    }

    public boolean isGlowingText() {
        return data.isGlowingText();
    }

    public boolean setGlowingText(boolean glowingText) {
        boolean result = this.data.setGlowingText(glowingText);
        if (result) this.updateListeners();
        return result;
    }

    private void updateListeners() {
        this.markDirty();
        this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public PlaqueType getPlaqueType() {
        Block block = this.getCachedState().getBlock();
        if (block instanceof PlaqueBlock plaqueBlock) {
            return plaqueBlock.getPlaqueType();
        }
        throw new IllegalStateException("PlaqueBlockEntity is not a PlaqueBlock");
    }

    public String[] getText(boolean filtered) {
        String[] strings = new String[getLineCount()];

        for (int i = 0; i < getLineCount(); ++i) {
            strings[i] = this.getTextOnRow(i, filtered).getString();
        }

        return strings;
    }

    public int getMaxTextWidth() {
        return PlaqueData.getMaxLength();
    }

    public int getTextLineHeight() {
        return PlaqueData.getLineHeight();
    }

    @Override
    public String toString() {
        return "PlaqueBlockEntity{" + "pos=" + pos + '}';
    }
}

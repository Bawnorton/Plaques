package com.bawnorton.plaques.block.entity;

import com.bawnorton.plaques.util.PlaqueAccents;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaqueData {
    private static final int LINE_COUNT = 3;
    private static final int MAX_LENGTH = 55;
    private static final int LINE_HEIGHT = 12;

    public static final String[] TEXT_KEYS = new String[LINE_COUNT];
    public static final String[] FILTERED_TEXT_KEYS = new String[LINE_COUNT];

    private final List<Text> texts;
    private final List<Text> filteredTexts;
    private boolean editable;
    @Nullable
    private UUID editor;
    @Nullable
    private OrderedText[] textsBeingEdited;
    private boolean filterText;
    private PlaqueAccents textColour;
    private boolean glowingText;

    public PlaqueData() {
        this.texts = new ArrayList<>(){{for(int i = 0; i < LINE_COUNT; ++i) add(ScreenTexts.EMPTY);}};
        this.filteredTexts = new ArrayList<>(){{for(int i = 0; i < LINE_COUNT; ++i) add(ScreenTexts.EMPTY);}};
        this.editable = true;
        this.textColour = PlaqueAccents.NONE;
    }

    static {
        for(int i = 0; i < LINE_COUNT; ++i) {
            TEXT_KEYS[i] = "Text" + (i + 1);
            FILTERED_TEXT_KEYS[i] = "FilteredText" + (i + 1);
        }
    }

    public void writeNbt(NbtCompound nbt) {
        for(int i = 0; i < LINE_COUNT; ++i) {
            Text text = this.texts.get(i);
            String string = Text.Serializer.toJson(text);
            nbt.putString(TEXT_KEYS[i], string);
            Text text2 = this.filteredTexts.get(i);
            if (!text2.equals(text)) {
                nbt.putString(FILTERED_TEXT_KEYS[i], Text.Serializer.toJson(text2));
            }
        }

        nbt.putString("Color", this.textColour.getName());
        nbt.putBoolean("GlowingText", this.glowingText);
    }

    public void readNbt(NbtCompound nbt, World world, BlockPos pos) {
        this.textColour = PlaqueAccents.byName(nbt.getString("Color"), PlaqueAccents.NONE);

        for(int i = 0; i < LINE_COUNT; ++i) {
            String string = nbt.getString(TEXT_KEYS[i]);
            Text text = parseTextFromJson(string, world, pos);
            this.texts.set(i, text);
            String string2 = FILTERED_TEXT_KEYS[i];
            if (nbt.contains(string2, 8)) {
                this.filteredTexts.set(i, parseTextFromJson(nbt.getString(string2), world, pos));
            } else {
                this.filteredTexts.set(i, text);
            }
        }

        this.textsBeingEdited = null;
        this.glowingText = nbt.getBoolean("GlowingText");
    }

    private Text parseTextFromJson(String json, World world, BlockPos pos) {
        Text text = this.unparsedTextFromJson(json);
        if (world instanceof ServerWorld serverWorld) {
            try {
                return Texts.parse(this.getCommandSource(null, serverWorld, pos), text, null, 0);
            } catch (CommandSyntaxException ignored) {
            }
        }

        return text;
    }

    private Text unparsedTextFromJson(String json) {
        try {
            Text text = Text.Serializer.fromJson(json);
            if (text != null) {
                return text;
            }
        } catch (Exception ignored) {
        }

        return ScreenTexts.EMPTY;
    }

    public ServerCommandSource getCommandSource(@Nullable ServerPlayerEntity player, ServerWorld world, BlockPos pos) {
        String string = player == null ? "Plaque" : player.getName().getString();
        Text text = player == null ? Text.literal("Plaque") : player.getDisplayName();
        return new ServerCommandSource(CommandOutput.DUMMY, Vec3d.ofCenter(pos), Vec2f.ZERO, world, 2, string, text, world.getServer(), player);
    }

    public List<Text> getTexts() {
        return this.texts;
    }

    public List<Text> getFilteredTexts() {
        return this.filteredTexts;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Nullable
    public UUID getEditor() {
        return this.editor;
    }

    public void setEditor(@Nullable UUID editor) {
        this.editor = editor;
    }

    @Nullable
    public OrderedText[] getTextsBeingEdited() {
        return this.textsBeingEdited;
    }

    public void setTextsBeingEdited(@Nullable OrderedText[] textsBeingEdited) {
        this.textsBeingEdited = textsBeingEdited;
    }

    public boolean isFilterText() {
        return this.filterText;
    }

    public void setFilterText(boolean filterText) {
        this.filterText = filterText;
    }

    public PlaqueAccents getTextColour() {
        return this.textColour;
    }

    public boolean setTextColour(PlaqueAccents textColour) {
        if (getTextColour() == textColour) {
            return false;
        }
        this.textColour = textColour;
        return true;
    }

    public boolean isGlowingText() {
        return this.glowingText;
    }

    public boolean setGlowingText(boolean glowingText) {
        if (isGlowingText() == glowingText) {
            return false;
        }
        this.glowingText = glowingText;
        return true;
    }

    public static int getLineCount() {
        return LINE_COUNT;
    }

    public static int getMaxLength() {
        return MAX_LENGTH;
    }

    public static int getLineHeight() {
        return LINE_HEIGHT;
    }
}

package com.bawnorton.plaques.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.StringIdentifiable;

public enum PlaqueAccents implements StringIdentifiable {
    IRON(0xD0D0D0, Items.IRON_INGOT),
    GOLD(0xFDF64E, Items.GOLD_INGOT),
    NETHERITE(0x3D383B, Items.NETHERITE_INGOT),
    COPPER(0xDF6744, Items.COPPER_INGOT),
    DIAMOND(0x41ECD0, Items.DIAMOND),
    EMERALD(0x3CEE6F, Items.EMERALD),
    LAPIS(0x2847B5, Items.LAPIS_LAZULI),
    REDSTONE(0xFB0006, Items.REDSTONE),
    AMETHYST(0xA274F0, Items.AMETHYST_SHARD),
    NONE(0x000000, Items.AIR);

    private final int colour;
    private final Item accent;

    PlaqueAccents(int colour, Item accent) {
        this.colour = colour;
        this.accent = accent;
    }

    public static boolean hasAccent(Item item) {
        for (PlaqueAccents colour : values()) {
            if (colour.accent == item) {
                return true;
            }
        }
        return false;
    }

    public int getColour() {
        return colour;
    }

    public int[] getColourArray() {
        return new int[] {
                (getColour() >> 16 & 0xFF),
                (getColour() >> 8 & 0xFF),
                (getColour() & 0xFF)
        };
    }

    public String getName() {
        return name().toLowerCase();
    }

    public static PlaqueAccents byName(String name, PlaqueAccents def) {
        for (PlaqueAccents colour : values()) {
            if (colour.getName().toLowerCase().equals(name)) {
                return colour;
            }
        }
        return def;
    }

    public static PlaqueAccents byItem(Item item) {
        for (PlaqueAccents colour : values()) {
            if (colour.accent == item) {
                return colour;
            }
        }
        return NONE;
    }

    @Override
    public String asString() {
        return name().toLowerCase();
    }
}

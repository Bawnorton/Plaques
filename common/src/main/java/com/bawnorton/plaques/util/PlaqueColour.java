package com.bawnorton.plaques.util;

import net.minecraft.util.StringIdentifiable;

public enum PlaqueColour implements StringIdentifiable {
    IRON(0xD0D0D0),
    GOLD(0xFDF64E),
    NETHERITE(0x3D383B),
    COPPER(0xDF6744),
    DIAMOND(0x41ECD0),
    EMERALD(0x3CEE6F),
    LAPIS(0x2847B5),
    REDSTONE(0xFB0006),
    QUARTZ(0xF5F2EE),
    NONE(0x000000);

    private final int colour;

    PlaqueColour(int colour) {
        this.colour = colour;
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

    public static PlaqueColour byName(String name, PlaqueColour def) {
        for (PlaqueColour colour : values()) {
            if (colour.getName().toLowerCase().equals(name)) {
                return colour;
            }
        }
        return def;
    }

    @Override
    public String asString() {
        return name().toLowerCase();
    }
}

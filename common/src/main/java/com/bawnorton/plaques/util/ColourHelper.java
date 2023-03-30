package com.bawnorton.plaques.util;

public abstract class ColourHelper {
    public static int darken(int colour, double amount) {
        return mix(colour, 0x000000, amount);
    }

    public static float darken(float colour, double amount) {
        return (float) (colour * (1 - amount));
    }

    public static int lighten(int colour, double amount) {
        return mix(colour, 0xFFFFFF, amount);
    }

    public static float lighten(float colour, double amount) {
        return (float) (colour + (1 - colour) * amount);
    }

    public static int mix(int colour1, int colour2, double amount) {
        int r1 = (colour1 >> 16 & 0xFF);
        int g1 = (colour1 >> 8 & 0xFF);
        int b1 = (colour1 & 0xFF);
        int r2 = (colour2 >> 16 & 0xFF);
        int g2 = (colour2 >> 8 & 0xFF);
        int b2 = (colour2 & 0xFF);
        int r = (int) (r1 * (1 - amount) + r2 * amount);
        int g = (int) (g1 * (1 - amount) + g2 * amount);
        int b = (int) (b1 * (1 - amount) + b2 * amount);
        return (r << 16) + (g << 8) + b;
    }
}

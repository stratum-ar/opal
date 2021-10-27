package com.stratum.uiserver.graphics;

public class ColorUtil {
    public static short pack(float red, float green, float blue) {
        int r = (int) (red * 31);
        int g = (int) (green * 7);
        int b = (int) (blue * 15);

        return (short) (g | (r << 3) | (b << 8));
    }
}

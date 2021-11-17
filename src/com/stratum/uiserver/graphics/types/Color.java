package com.stratum.uiserver.graphics.types;

public class Color implements IFill {
    private final float red;
    private final float green;
    private final float blue;

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float red() {
        return red;
    }

    public float green() {
        return green;
    }

    public float blue() {
        return blue;
    }

    @Override
    public Color sample(int x, int y) {
        return this;
    }

    public static Color BLACK = new Color(0f, 0f, 0f);
    public static Color WHITE = new Color(1f, 1f, 1f);
    public static Color RED = new Color(1f, 0f, 0f);
    public static Color YELLOW = new Color(1f, 1f, 0f);
    public static Color GREEN = new Color(0f, 1f, 0f);
    public static Color CYAN = new Color(0f, 1f, 1f);
    public static Color BLUE = new Color(0f, 0f, 1f);
    public static Color MAGENTA = new Color(1f, 0f, 1f);
}

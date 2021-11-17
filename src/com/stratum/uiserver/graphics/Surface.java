package com.stratum.uiserver.graphics;

import com.stratum.uiserver.graphics.types.Color;

public class Surface {
    private final Color[][] pixels;

    private final int width;
    private final int height;

    public Surface(int w, int h) {
        width = w;
        height = h;

        pixels = new Color[width][height];
    }

    public Surface() {
        width = 240;
        height = 240;

        pixels = new Color[width][height];
    }

    public Color[][] getPixels() {
        return pixels;
    }

    public Color getPixel(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return pixels[x][y];
        }

        return null;
    }

    public void setPixel(int x, int y, Color color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[x][y] = color;
        }
    }

    public void clear(Color color) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = color;
            }
        }
    }

    public Graphics getGraphics() {
        return new Graphics(this);
    }
}

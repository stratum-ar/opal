package com.stratum.uiserver.graphics;

public class Surface {
    private final short[][] pixels;

    private final int width;
    private final int height;

    public Surface(int w, int h) {
        width = w;
        height = h;

        pixels = new short[width][height];
    }

    public Surface() {
        width = 240;
        height = 240;

        pixels = new short[width][height];
    }

    public short[][] getPixels() {
        return pixels;
    }

    public short getPixel(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return pixels[x][y];
        }

        return 0;
    }

    public void setPixel(int x, int y, short color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[x][y] = color;
        }
    }

    public void clear(short color) {
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

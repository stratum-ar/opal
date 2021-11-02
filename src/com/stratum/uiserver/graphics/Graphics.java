package com.stratum.uiserver.graphics;

public class Graphics {
    private final Surface surf;

    public Graphics(Surface surface) {
        surf = surface;
    }

    public void fillRect(int x, int y, int width, int height, short color) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                surf.setPixel(x + dx, y + dy, color);
            }
        }
    }
}

package com.stratum.opal.graphics.basicgraphics;

import com.stratum.opal.graphics.Surface;

public class BasicGraphicsSurface extends Surface {
    public void fillRect(int x, int y, int width, int height, short color) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                setPixel(x + dx, y + dy, color);
            }
        }
    }
}

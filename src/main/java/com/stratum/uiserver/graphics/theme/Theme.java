package com.stratum.uiserver.graphics.theme;

import com.stratum.uiserver.graphics.MathUtil;
import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.icons.IconSet;
import com.stratum.uiserver.graphics.types.Color;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Theme {
    private final byte[] themeData;

    public Theme(byte[] themeData) {
        this.themeData = themeData;
    }

    private Color drawElementPixel(Surface surface, ThemeItem item, int itemX, int itemY, int width, int height) {
        int index = item.compareTo(ThemeItem.BUTTON);

        int x = MathUtil.border(itemX, 3, width, 16);
        int y = MathUtil.border(itemY, 3, height, 16);

        int dataWidth = themeData.length / 16;
        int dataOffset = y * dataWidth + (x + index * 16) * 3;

        return new Color(
                (float)themeData[dataOffset] / 127,
                (float)themeData[dataOffset + 1] / 127,
                (float)themeData[dataOffset + 2] / 127
        );
    }

    public void drawElement(Surface surface, ThemeItem item, int x, int y, int width, int height) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                surface.setPixel(x + dx, y + dy, drawElementPixel(surface, item, dx, dy, width, height));
            }
        }
    }

    public static Theme load(InputStream inputStream) throws IOException {
        return new Theme(IOUtils.toByteArray(inputStream));
    }
}

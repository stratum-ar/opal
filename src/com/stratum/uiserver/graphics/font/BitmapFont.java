package com.stratum.uiserver.graphics.font;

import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.types.Color;
import com.stratum.uiserver.graphics.types.IFill;

public class BitmapFont implements IFont {
    private int[] codePoints;
    private int[] characterWidths;
    private int[] characterXOffsets;

    private int bitmapDataWidth;
    private int lineHeight;
    private byte[] bitmapData;

    protected void setFontData(
            int[] codePoints, int[] characterWidths, int[] characterXOffsets,
            int bitmapDataWidth, int lineHeight, byte[] bitmapData
    ) {
        this.codePoints = codePoints;
        this.characterWidths = characterWidths;
        this.characterXOffsets = characterXOffsets;
        this.bitmapDataWidth = bitmapDataWidth;
        this.lineHeight = lineHeight;
        this.bitmapData = bitmapData;
    }

    @Override
    public int getLineHeight() {
        return lineHeight;
    }

    private int getCharacterIndex(char chr) {
        for (int i = 0; i < codePoints.length; i++) {
            if (codePoints[i] == (int)chr) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int measureString(String string) {
        int width = 0;

        for (char chr : string.toCharArray()) {
            int index = getCharacterIndex(chr);
            if (index == -1) {
                continue;
            }

            width += characterWidths[index];
        }

        return width;
    }

    private void drawCharacter(Surface surface, int index, int x, int y, IFill fill) {
        int chrXOffset = characterXOffsets[index];

        for (int dx = 0; dx < characterWidths[index]; dx++) {
            for (int dy = 0; dy < lineHeight; dy++) {
                int dataOffset = dy * bitmapDataWidth + chrXOffset + dx;
                byte v = bitmapData[dataOffset];

                if (v > 0) {
                    surface.setPixel(
                            x + dx, y + dy,
                            Color.lerp(
                                    (float)v / 127f,
                                    surface.getPixel(x, y),
                                    fill.sample(x, y)
                            )
                    );
                }
            }
        }
    }

    @Override
    public void drawText(Surface surface, String string, int x, int y, IFill fill) {
        int xOffset = 0;

        for (char chr : string.toCharArray()) {
            int index = getCharacterIndex(chr);

            if (index == -1) {
                continue;
            }

            drawCharacter(surface, index, x + xOffset, y, fill);
            xOffset += characterWidths[index];
        }
    }
}

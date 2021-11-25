package com.stratum.uiserver.graphics.icons;

import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.types.Color;
import com.stratum.uiserver.graphics.types.IFill;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class IconSet {
    private final byte[] iconData;

    public IconSet(byte[] iconData) {
        this.iconData = iconData;
    }

    public void drawIcon(Surface surface, Icons icon, int x, int y, IFill fill) {
        int index = icon.compareTo(Icons.NONE);
        int xOffset = (index % 16) * 16;
        int yOffset = (index / 16) * 16;

        for (int dx = 0; dx < 16; dx++) {
            for (int dy = 0; dy < 16; dy++) {
                int byteOffset = (yOffset + dy) * 256 + xOffset + dx;
                byte v = iconData[byteOffset];

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

    public static IconSet load(URL filePath) throws IOException, URISyntaxException {
        File file = new File(filePath.toURI());

        return new IconSet(Files.readAllBytes(file.toPath()));
    }
}

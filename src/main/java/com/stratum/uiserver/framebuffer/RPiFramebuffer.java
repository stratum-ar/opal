package com.stratum.uiserver.framebuffer;

import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.types.Color;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RPiFramebuffer implements IFramebuffer {
    private int rpiBytesFromStratumColor(Color color) {
        if (color == null) {
            return 0;
        }

        int r = (int) (color.red() * 31);
        int g = (int) (color.green() * 7);
        int b = (int) (color.blue() * 15);

        return (g | (r << 3) | (b << 8));
    }

    @Override
    public void write(Surface surf) {
        try {
            File fb1 = new File("/dev/fb1");
            FileOutputStream outputStream = new FileOutputStream(fb1, false);
            DataOutputStream stream = new DataOutputStream(outputStream);

            Color[][] pixels = surf.getPixels();

            byte[] fbBytes = new byte[240 * 240 * 2];

            for (int y = 0; y < 240; y++) {
                for (int x = 0; x < 240; x++) {
                    int offset = y * 240 + x;
                    int pixel = rpiBytesFromStratumColor(pixels[x][y]);

                    fbBytes[2 * offset] = (byte) (pixel >> 8);
                    fbBytes[2 * offset + 1] = (byte) (pixel & 0xFF);
                }
            }

            stream.write(fbBytes);

            stream.flush();
            stream.close();

            outputStream.close();
        } catch (IOException e) {
            System.out.println("Tried to write to /dev/fb1, failed.");
        }
    }
}

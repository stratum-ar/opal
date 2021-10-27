package com.stratum.uiserver.framebuffer;

import com.stratum.uiserver.graphics.Surface;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RPiFramebuffer implements IFramebuffer {
    @Override
    public void write(Surface surf) {
        try {
            File fb1 = new File("/dev/fb1");
            FileOutputStream outputStream = new FileOutputStream(fb1, false);
            DataOutputStream stream = new DataOutputStream(outputStream);

            short[][] pixels = surf.getPixels();

            byte[] fbBytes = new byte[240 * 240 * 2];

            for (int y = 0; y < 240; y++) {
                for (int x = 0; x < 240; x++) {
                    int offset = y * 240 + x;
                    int pixel = pixels[x][y];

                    fbBytes[2 * offset] = (byte)(pixel >> 8);
                    fbBytes[2 * offset + 1] = (byte)(pixel & 0xFF);
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

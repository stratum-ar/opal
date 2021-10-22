package com.stratum.opal;

import com.stratum.opal.framebuffer.IFramebuffer;
import com.stratum.opal.framebuffer.RPiFramebuffer;
import com.stratum.opal.framebuffer.SwingFramebuffer;
import com.stratum.opal.graphics.Surface;

public class OpalApp {
    public static void testDraw(IFramebuffer framebuffer) {
        Surface surf = new Surface();
        surf.clear((short)0x0FF0);

        framebuffer.write(surf);
    }

    public static void main(String[] argv) {
        if (argv.length == 1) {
            IFramebuffer framebuffer;

            if (argv[0].equals("fb1")) {
                framebuffer = new RPiFramebuffer();
            } else if (argv[0].equals("swing")) {
                framebuffer = new SwingFramebuffer();
            } else {
                System.out.println("Invalid mode.");
                return;
            }

            System.out.println(43);
            testDraw(framebuffer);
        } else {
            System.out.println("Usage: opal [mode]");
            System.out.println("Modes:");
            System.out.println("\tswing - display framebuffer in a window");
            System.out.println("\tfb1 - send framebuffer to /dev/fb1 (RPi only)");
        }
    }
}

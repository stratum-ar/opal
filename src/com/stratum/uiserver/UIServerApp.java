package com.stratum.uiserver;

import com.stratum.uiserver.framebuffer.*;
import com.stratum.uiserver.graphics.ColorUtil;
import com.stratum.uiserver.graphics.basicgraphics.BasicGraphicsSurface;

public class UIServerApp {
    public static void testDraw(IFramebuffer framebuffer) {
        BasicGraphicsSurface surf = new BasicGraphicsSurface();

        surf.fillRect(40, 40, 80, 50, ColorUtil.pack(1.f, 1.f, 0.f));
        surf.fillRect(120, 90, 60, 60, ColorUtil.pack(1.f, 0.f, 1.f));

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

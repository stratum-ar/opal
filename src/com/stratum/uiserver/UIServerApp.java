package com.stratum.uiserver;

import com.stratum.uiserver.connection.RequestReader;
import com.stratum.uiserver.framebuffer.*;
import com.stratum.uiserver.graphics.Graphics;
import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.ui.UIPrimitives;
import com.stratum.uiserver.graphics.font.BitmapFont;
import com.stratum.uiserver.graphics.icons.IconSet;
import com.stratum.uiserver.graphics.icons.Icons;
import com.stratum.uiserver.graphics.theme.Theme;
import com.stratum.uiserver.graphics.types.Color;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class UIServerApp {
    public static void testDraw(IFramebuffer framebuffer) {
        // Use for testing out changes to the drawing API

        Surface surf = new Surface();
        Graphics gfx = new Graphics(surf);

        try {
            Theme theme = Theme.load(Objects.requireNonNull(UIServerApp.class.getResource("/default_theme.bin")));
            IconSet iconSet = IconSet.load(Objects.requireNonNull(UIServerApp.class.getResource("/default_icons.bin")));
            BitmapFont font = BitmapFont.load(Objects.requireNonNull(UIServerApp.class.getResource("/default_font.bin")));

            UIPrimitives ui = new UIPrimitives(theme, iconSet, font);

            ui.drawCheckbox(surf, 16, 16, false);
            font.drawText(surf, "Checkbox unchecked", 32, 16, Color.WHITE);
            ui.drawCheckbox(surf, 16, 32, true);
            font.drawText(surf, "Checkbox checked", 32, 32, Color.WHITE);

            ui.drawButton(surf, "Button", 16, 48, 60, 20, false, false);
            ui.drawButton(surf, "Button", 80, 48, 60, 20, true, false);
            ui.drawButton(surf, "Button", 144, 48, 60, 20, false, true);

            ui.drawHorizontalSlider(surf, 0.25, 16, 70, 120, 16);
            ui.drawHorizontalSlider(surf, 0.5, 16, 86, 120, 16);
            ui.drawHorizontalSlider(surf, 0.75, 16, 102, 120, 16);

            ui.drawVerticalSlider(surf, 0.25, 136, 70, 16, 100);
            ui.drawVerticalSlider(surf, 0.5, 152, 70, 16, 100);
            ui.drawVerticalSlider(surf, 0.75, 168, 70, 16, 100);

            ui.drawComboBox(surf, "ComboBox", 16, 120, 100, 20);

            ui.drawPager(surf, 4, 1, 16, 208, 208, 16, false);

            framebuffer.write(surf);
        } catch (Exception ignored) {}
    }

    public static void main(String[] argv) {
        try {
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

                testDraw(framebuffer);

                start(50666, framebuffer);
            } else {
                System.out.println("Usage: opal [mode]");
                System.out.println("Modes:");
                System.out.println("\tswing - display framebuffer in a window");
                System.out.println("\tfb1 - send framebuffer to /dev/fb1 (RPi only)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //example: echo -e '\x1\x12\x20\x40\x40\x40\x40\x40\x40\x02' | nc localhost 50666 {one rectangle}
    //example: echo -e '\x2\x12\x20\x40\x40\x40\x40\x40\x40\x01\x12\x50\x60\x40\x30\x02\x40\x40\x02' | nc localhost 50666 {two rectangles}
    //example: echo -e '\x3\x12\x20\x40\x40\x40\x40\x40\x40\x01\x12\x50\x60\x40\x30\x02\x40\x40\x01\x11\x80\x90\x40\x30\x80\x40\x40\x02' | nc localhost 50666 {two rectangles and ellipse}
    //example: echo -e '\x1\x17\x5\x78\x32\x3c\x64\x46\xc8\xaa\xc8\xb4\x64\x80\x40\x40\x02' | nc localhost 50666
    // echo -e '\x1\x1e\x12\x20\x23\x54\x68\x65\x20\x71\x75\x69\x63\x6b\x20\x62\x72\x6f\x77\x6e\x20\x6a\x75\x6d\x70\x73\x20\x6f\x76\x65\x72\x20\x31\x33\x20\x6c\x61\x7a\x79\x2e\x80\x40\x40\x02' | nc localhost 50666
    public static void start(int port, IFramebuffer framebuffer) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream((clientSocket.getOutputStream()));

                Surface surface = new RequestReader(in).readRequest();

                framebuffer.write(surface);

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

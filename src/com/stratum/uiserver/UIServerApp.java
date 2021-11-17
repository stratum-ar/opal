package com.stratum.uiserver;

import com.stratum.uiserver.connection.RequestReader;
import com.stratum.uiserver.framebuffer.*;
import com.stratum.uiserver.graphics.ColorUtil;
import com.stratum.uiserver.graphics.Graphics;
import com.stratum.uiserver.graphics.Surface;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class UIServerApp {
    public static void testDraw(IFramebuffer framebuffer) {
        // Use for testing out changes to the drawing API
        // Keep empty in main thank you
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

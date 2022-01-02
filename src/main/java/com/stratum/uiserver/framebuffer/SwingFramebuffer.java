package com.stratum.uiserver.framebuffer;

import com.stratum.uiserver.graphics.Surface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SwingFramebuffer implements IFramebuffer {
    private final JFrame frame;
    private final FramebufferPanel panel;

    private String apiServerHostname;

    public SwingFramebuffer() {
        frame = new JFrame("OpalServer");
        panel = new FramebufferPanel();

        prepareFrame();
    }

    public void setApiServerHostname(String apiServerHostname) {
        this.apiServerHostname = apiServerHostname;
    }

    private void prepareFrame() {
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void write(Surface surf) {
        panel.setSurface(surf);
        panel.repaint();
    }

    public void sendInput(int x, int y, boolean isDragged) {
        if (apiServerHostname == null) {
            return;
        }

        try {
            Socket socket = new Socket(apiServerHostname, 50665);

            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
            stream.writeByte(1); // Input
            stream.writeByte(x);
            stream.writeByte(y);
            stream.writeByte(isDragged ? 1 : 0);
            stream.flush();

            socket.close();
            stream.close();
        } catch (IOException ignored) {}
    }

    private class FramebufferPanel extends JPanel {
        private Surface surface = new Surface();

        public FramebufferPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    sendInput(e.getX() / 2, e.getY() / 2, false);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    sendInput(e.getX() / 2, e.getY() / 2, true);
                }
            });
        }

        private Color colorFromStratumColor(com.stratum.uiserver.graphics.types.Color color) {
            if (color == null) {
                return Color.BLACK;
            }

            return new Color(color.red(), color.green(), color.blue());
        }

        public void setSurface(Surface surface) {
            this.surface = surface;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(480, 480);
        }

        @Override
        public void paint(Graphics g) {
            com.stratum.uiserver.graphics.types.Color[][] pixels = surface.getPixels();

            for (int x = 0; x < 240; x++) {
                for (int y = 0; y < 240; y++) {
                    g.setColor(colorFromStratumColor(pixels[x][y]));
                    g.fillRect(x * 2, y * 2, 2, 2);
                }
            }
        }
    }
}

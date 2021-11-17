package com.stratum.uiserver.framebuffer;

import com.stratum.uiserver.graphics.Surface;

import javax.swing.*;
import java.awt.*;

public class SwingFramebuffer implements IFramebuffer {
    private final JFrame frame;
    private final FramebufferPanel panel;

    public SwingFramebuffer() {
        frame = new JFrame("OpalServer");
        panel = new FramebufferPanel();

        prepareFrame();
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

    private class FramebufferPanel extends JPanel {
        private Surface surface = new Surface();

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

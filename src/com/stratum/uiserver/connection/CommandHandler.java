package com.stratum.uiserver.connection;

import com.stratum.uiserver.annotation.RequestMethod;
import com.stratum.uiserver.graphics.Graphics;
import com.stratum.uiserver.graphics.types.Color;
import com.stratum.uiserver.graphics.types.IFill;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class CommandHandler {
    private final Graphics graphics;

    public CommandHandler(Graphics graphics) {
        this.graphics = graphics;
    }

    public void runCommand(int commandNo, DataInputStream in) {
        Method[] methods = this.getClass().getMethods();
        try {
            for (Method method : methods) {

                if (((AnnotatedElement) method).isAnnotationPresent(RequestMethod.class)) {
                    RequestMethod requestMethod = ((AnnotatedElement) method).getAnnotation(RequestMethod.class);

                    if (commandNo == requestMethod.commandNo()) {
                        method.invoke(this, in);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMethod(commandNo = 16)
    public void drawLine(DataInputStream in) throws IOException {
        int sx = in.readUnsignedByte();
        int sy = in.readUnsignedByte();
        int ex = in.readUnsignedByte();
        int ey = in.readUnsignedByte();
        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        graphics.drawLine(sx, sy, ex, ey, fill);
    }

    @RequestMethod(commandNo = 17)
    public void fillEllipse(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        graphics.fillEllipse(x, y, width, height, fill);
    }

    @RequestMethod(commandNo = 18)
    public void fillRect(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        graphics.fillRect(x, y, width, height, fill);
    }

    @RequestMethod(commandNo = 20)
    public void drawQuadratic(DataInputStream in) throws IOException {
        int sx = in.readUnsignedByte();
        int sy = in.readUnsignedByte();
        int cx = in.readUnsignedByte();
        int cy = in.readUnsignedByte();
        int ex = in.readUnsignedByte();
        int ey = in.readUnsignedByte();
        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        graphics.drawQuadratic(sx, sy, cx, cy, ex, ey, fill);
    }

    @RequestMethod(commandNo = 23)
    public void drawPolygon(DataInputStream in) throws IOException {
        int pointsNo = in.readUnsignedByte();
        int[] xs = new int[pointsNo];
        int[] ys = new int[pointsNo];

        for (int i = 0; i < pointsNo; i++) {
            xs[i] = in.readUnsignedByte();
            ys[i] = in.readUnsignedByte();
        }
        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        graphics.drawPolygon(xs, ys, fill, false);
    }
}

package com.stratum.uiserver.graphics;

import com.stratum.uiserver.annotation.RequestMethod;
import com.stratum.uiserver.graphics.types.Color;
import com.stratum.uiserver.graphics.types.IFill;
import com.stratum.uiserver.graphics.types.Rect;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.function.BiFunction;

public class Graphics {
    private final Surface surf;

    private int bezierResolution = 10;
    private BiFunction<Short, Short, Short> blendingFunction = (bg, fg) -> fg;

    public Graphics(Surface surface) {
        surf = surface;
    }

    public int getBezierResolution() {
        return bezierResolution;
    }

    public void setBezierResolution(int bezierResolution) {
        this.bezierResolution = bezierResolution;
    }

    public BiFunction<Short, Short, Short> getBlendingFunction() {
        return blendingFunction;
    }

    public void setBlendingFunction(BiFunction<Short, Short, Short> blendingFunction) {
        this.blendingFunction = blendingFunction;
    }

    private void setPixel(int x, int y, short color) {
        surf.setPixel(x, y, blendingFunction.apply(surf.getPixel(x, y), color));
    }

    public Rect calcBoundingRect(int[] xs, int[] ys) {
        if (xs.length < 1 || ys.length < 1) {
            throw new IllegalArgumentException("There must be at least one point.");
        }
        if (xs.length != ys.length) {
            throw new IllegalArgumentException("There must be the same amount of both coordinates.");
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int i = 0; i < xs.length; i++) {
            minX = Math.min(minX, xs[i]);
            maxX = Math.max(maxX, xs[i]);
            minY = Math.min(minY, ys[i]);
            maxY = Math.max(maxY, ys[i]);
        }

        return new Rect(minX, minY, maxX - minX, maxY - minY);
    }

    public void drawLine(int sx, int sy, int ex, int ey, IFill fill) {
        int steps = Math.max(Math.abs(sx - ex), Math.abs(sy - ey));
        float dx = (float)(ex - sx) / steps;
        float dy = (float)(ey - sy) / steps;

        float fx = sx;
        float fy = sy;

        for (int i = 0; i < steps; i++) {
            setPixel((int)fx, (int)fy, fill.sample((int)fx, (int)fy));

            fx += dx;
            fy += dy;
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

        int steps = Math.max(Math.abs(sx - ex), Math.abs(sy - ey));
        float dx = (float) (ex - sx) / steps;
        float dy = (float) (ey - sy) / steps;

        float fx = sx;
        float fy = sy;

        for (int i = 0; i < steps; i++) {
            setPixel((int) fx, (int) fy, fill.sample((int) fx, (int) fy));

            fx += dx;
            fy += dy;
        }
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

        int[] xs = new int[bezierResolution];
        int[] ys = new int[bezierResolution];

        for (int i = 0; i < bezierResolution; i++) {
            float t = (float) i / (bezierResolution - 1);

            xs[i] = (int) MathUtil.lerp(
                    t, MathUtil.lerp(t, (float) sx, (float) cx), MathUtil.lerp(t, (float) cx, (float) ex)
            );
            ys[i] = (int) MathUtil.lerp(
                    t, MathUtil.lerp(t, (float) sy, (float) cy), MathUtil.lerp(t, (float) cy, (float) ey)
            );
        }

        drawPolygon(xs, ys, fill, true);
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

        drawPolygon(xs, ys, fill, false);
    }

    public void drawPolygon(int[] xs, int[] ys, IFill fill, boolean open) {
        if (xs.length < 2 || ys.length < 2) {
            throw new IllegalArgumentException("Polygon must contain at least 2 points.");
        }
        if (xs.length != ys.length) {
            throw new IllegalArgumentException("There must be the same amount of both coordinates.");
        }

        for (int i = 0; i < (open ? (xs.length - 1) : xs.length); i++) {
            drawLine(xs[i], ys[i], xs[(i + 1) % xs.length], ys[(i + 1) % ys.length], fill);
        }
    }


    public void fillRect(int x, int y, int width, int height, IFill fill) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                setPixel(x + dx, y + dy, fill.sample(x + dx, y + dy));
            }
        }
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

        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                setPixel(x + dx, y + dy, fill.sample(x + dx, y + dy));
            }
        }
    }

    public void fillEllipse(int x, int y, int width, int height, IFill fill) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                float cx = 2 * (float) dx / (width - 1) - 1;
                float cy = 2 * (float) dy / (height - 1) - 1;

                if (cx * cx + cy * cy <= 1) {
                    setPixel(x + dx, y + dy, fill.sample(x + dx, y + dy));
                }
            }
        }
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

        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                float cx = 2 * (float) dx / (width - 1) - 1;
                float cy = 2 * (float) dy / (height - 1) - 1;

                if (cx * cx + cy * cy <= 1) {
                    setPixel(x + dx, y + dy, fill.sample(x + dx, y + dy));
                }
            }
        }
    }
}

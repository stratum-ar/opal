package com.stratum.uiserver.graphics;

public class MathUtil {
    public static float lerp(float x, float start, float end) {
        return start + x * (end - start);
    }

    public static float unlerp(float y, float start, float end) {
        return (y - start) / (end - start);
    }

    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(max, x));
    }

    public static float clamp(float x) {
        return clamp(x, 0f, 1f);
    }
    
    public static int border(int x, int margin, int size, int referenceSize) {
        if (x < margin) {
            return x;
        } else if (x >= size - margin) {
            return x - size + referenceSize;
        }

        return ((x - margin) % (referenceSize - 2 * margin)) + margin;
    }
}

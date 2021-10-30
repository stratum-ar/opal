package com.stratum.uiserver.graphics;

public class MathUtil {
    public static float lerp(float x, float start, float end) {
        return start + x * (end - start);
    }
}

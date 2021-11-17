package com.stratum.uiserver.graphics.types;

import com.stratum.uiserver.graphics.MathUtil;

public class Gradient implements IFill {
    private final int start;
    private final int end;
    private final boolean vertical;

    private final Color startColor;
    private final Color endColor;

    public Gradient(int start, int end, Color startColor, Color endColor, boolean vertical) {
        this.start = start;
        this.end = end;
        this.vertical = vertical;

        this.startColor = startColor;
        this.endColor = endColor;
    }

    @Override
    public Color sample(int x, int y) {
        float position = MathUtil.clamp(MathUtil.unlerp((float)(vertical ? y : x), (float)start, (float)end));

        return new Color(
                MathUtil.lerp(position, startColor.red(), endColor.red()),
                MathUtil.lerp(position, startColor.green(), endColor.green()),
                MathUtil.lerp(position, startColor.blue(), endColor.blue())
        );
    }
}

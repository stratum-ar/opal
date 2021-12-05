package com.stratum.uiserver.graphics.ui;

import com.stratum.uiserver.graphics.types.Color;
import com.stratum.uiserver.graphics.types.IFill;

public class DisabledFill implements IFill {
    @Override
    public Color sample(int x, int y) {
        if (((x ^ y) & 1) == 1) {
            return Color.GRAY;
        }

        return Color.BLACK;
    }
}

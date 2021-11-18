package com.stratum.uiserver.graphics.font;

import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.types.IFill;

public interface IFont {
    int getLineHeight();
    int measureString(String string);

    void drawText(Surface surface, String string, int x, int y, IFill fill);
}

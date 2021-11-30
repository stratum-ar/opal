package com.stratum.uiserver.graphics.ui;

import com.stratum.uiserver.graphics.MathUtil;
import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.font.IFont;
import com.stratum.uiserver.graphics.icons.IconSet;
import com.stratum.uiserver.graphics.icons.Icons;
import com.stratum.uiserver.graphics.theme.Theme;
import com.stratum.uiserver.graphics.theme.ThemeItem;
import com.stratum.uiserver.graphics.types.Color;
import com.stratum.uiserver.graphics.types.IFill;

public class UIPrimitives {
    private final Theme theme;
    private final IconSet iconSet;
    private final IFont font;

    public UIPrimitives(Theme theme, IconSet iconSet, IFont font) {
        this.theme = theme;
        this.iconSet = iconSet;
        this.font = font;
    }

    public void drawAlignedText(Surface surface, String text, int x, int y, int width, int height, IFill fill, Alignment alignment) {
        int textWidth = font.measureString(text);

        int dx = (width - textWidth) / 2;
        int dy = (height - font.getLineHeight()) / 2;

        if (alignment == Alignment.TOPLEFT || alignment == Alignment.TOP || alignment == Alignment.TOPRIGHT) {
            dy = 0;
        } else if (alignment == Alignment.BOTTOMLEFT || alignment == Alignment.BOTTOM || alignment == Alignment.BOTTOMRIGHT) {
            dy = height - font.getLineHeight();
        }

        if (alignment == Alignment.TOPLEFT || alignment == Alignment.LEFT || alignment == Alignment.BOTTOMLEFT) {
            dx = 0;
        } else if (alignment == Alignment.TOPRIGHT || alignment == Alignment.RIGHT || alignment == Alignment.BOTTOMRIGHT) {
            dx = width - textWidth;
        }

        font.drawText(surface, text, x + dx, y + dy, fill);
    }

    public void drawCheckbox(Surface surface, int x, int y, boolean checked) {
        theme.drawElement(
                surface,
                checked ? ThemeItem.CHECKBOX_CHECKED : ThemeItem.CHECKBOX,
                x, y, 16, 16
        );
        if (checked) {
            iconSet.drawIcon(surface, Icons.OK_SMALL, x, y, Color.WHITE);
        }
    }

    public void drawButton(Surface surface, String text, int x, int y, int width, int height, boolean pressed, boolean highlighted) {
        ThemeItem themeItem = ThemeItem.BUTTON;
        if (highlighted) {
            themeItem = ThemeItem.BUTTON_HIGHLIGHTED;
        }
        if (pressed) {
            themeItem = ThemeItem.BUTTON_PRESSED;
        }

        theme.drawElement(surface, themeItem, x, y, width, height);
        drawAlignedText(surface, text, x, y, width, height, pressed ? Color.BLACK : Color.WHITE, Alignment.MIDDLE);
    }

    public void drawComboBox(Surface surface, String text, int x, int y, int width, int height) {
        ThemeItem themeItem = ThemeItem.BUTTON;

        theme.drawElement(surface, themeItem, x, y, width, height);
        drawAlignedText(surface, text, x + 6, y, width - 22, height, Color.WHITE, Alignment.LEFT);
        iconSet.drawIcon(surface, Icons.COMBOBOX_UPDOWN, x + width - 16, y + (height - 16) / 2, Color.WHITE);
    }

    public void drawHorizontalSlider(Surface surface, double fillPercentage, int x, int y, int width, int height) {
        int fillWidth = (int)(fillPercentage * width);
        int thumbPosition = (int)MathUtil.lerp((float)fillPercentage, 0, width - 16);

        y += (height - 16) / 2;

        theme.drawElement(surface, ThemeItem.SLIDER_HORIZONTAL, x, y, width, 16);
        if (fillWidth >= 6) {
            theme.drawElement(surface, ThemeItem.SLIDER_HORIZONTAL_FILL, x, y, fillWidth, 16);
        }

        iconSet.drawIcon(surface, Icons.SLIDER_HORIZONTAL_THUMB_BG, x + thumbPosition, y, Color.BLACK);
        iconSet.drawIcon(surface, Icons.SLIDER_HORIZONTAL_THUMB_FG, x + thumbPosition, y, Color.WHITE);
    }

    public void drawVerticalSlider(Surface surface, double fillPercentage, int x, int y, int width, int height) {
        int fillHeight = (int)(fillPercentage * height);
        int thumbPosition = (int)MathUtil.lerp((float)fillPercentage, height - 16, 0);

        x += (width - 16) / 2;

        theme.drawElement(surface, ThemeItem.SLIDER_VERTICAL, x, y, 16, height);
        if (fillHeight >= 6) {
            theme.drawElement(surface, ThemeItem.SLIDER_VERTICAL_FILL, x, y + height - fillHeight, 16, fillHeight);
        }

        iconSet.drawIcon(surface, Icons.SLIDER_VERTICAL_THUMB_BG, x, y + thumbPosition, Color.BLACK);
        iconSet.drawIcon(surface, Icons.SLIDER_VERTICAL_THUMB_FG, x, y + thumbPosition, Color.WHITE);
    }

    public void drawPager(Surface surface, int items, int selected, int x, int y, int width, int height, boolean vertical) {
        int dx, dy;

        if (vertical) {
            dx = (width - 16) / 2;
            dy = (height - 16 * items) / 2;
        } else {
            dx = (width - 16 * items) / 2;
            dy = (height - 16) / 2;
        }

        for (int i = 0; i < items; i++) {
            iconSet.drawIcon(
                    surface,
                    (i == selected) ? Icons.PAGER_SELECTED : Icons.PAGER_TICK,
                    x + dx, y + dy,
                    (i == selected) ? Color.BLUE : Color.WHITE
            );

            if (vertical) {
                dy += 16;
            } else {
                dx += 16;
            }
        }
    }
}

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

    private final IFill disabledFill = new DisabledFill();

    public UIPrimitives(Theme theme, IconSet iconSet, IFont font) {
        this.theme = theme;
        this.iconSet = iconSet;
        this.font = font;
    }

    private void drawButtonFrame(Surface surface, int x, int y, int width, int height, ButtonState state) {
        switch (state) {
            case DEFAULT:
                theme.drawElement(surface, ThemeItem.BUTTON, x, y, width, height);
                break;
            case DISABLED:
                theme.drawElement(surface, ThemeItem.BUTTON_DISABLED, x, y, width, height);
                break;
            case HIGHLIGHTED:
                theme.drawElement(surface, ThemeItem.BUTTON_HIGHLIGHTED, x, y, width, height);
                break;
            case PRESSED:
                theme.drawElement(surface, ThemeItem.BUTTON_PRESSED, x, y, width, height);
                break;
        }
    }

    private IFill getButtonTextColor(ButtonState state) {
        switch (state) {
            case PRESSED:
                return Color.BLACK;
            case DISABLED:
                return disabledFill;
            default:
                return Color.WHITE;
        }
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

    public void drawButton(Surface surface, String text, Icons icon, int x, int y, int width, int height, ButtonState state) {
        IFill textColor = getButtonTextColor(state);

        drawButtonFrame(surface, x, y, width, height, state);

        if (icon == null) {
            drawAlignedText(surface, text, x, y, width, height, textColor, Alignment.MIDDLE);
        } else {
            int iconX = (width - font.measureString(text) - 18) / 2;
            if (text.length() == 0) {
                iconX += 1;
            }

            iconSet.drawIcon(surface, icon, x + iconX, y + (height - 16) / 2, textColor);
            drawAlignedText(surface, text, x + 9, y, width, height, textColor, Alignment.MIDDLE);
        }
    }

    public void drawComboBox(Surface surface, String text, int x, int y, int width, int height, ButtonState state) {
        IFill textColor = getButtonTextColor(state);

        drawButtonFrame(surface, x, y, width, height, state);

        drawAlignedText(surface, text, x + 6, y, width - 22, height, textColor, Alignment.LEFT);
        iconSet.drawIcon(surface, Icons.COMBOBOX_UPDOWN, x + width - 16, y + (height - 16) / 2, textColor);
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

    public void drawProgress(Surface surface, double progress, int x, int y, int width, int height) {
        int progressWidth = (int)(progress * width);

        theme.drawElement(surface, ThemeItem.BUTTON, x, y, width, height);

        if (progressWidth >= 6) {
            theme.drawElement(surface, ThemeItem.PROGRESS_FILL, x, y, progressWidth, height);
        }
    }

    private void drawEditBoxFrame(Surface surface, int x, int y, int width, int height, EditBoxState state) {
        switch (state) {
            case DEFAULT:
                theme.drawElement(surface, ThemeItem.EDITBOX, x, y, width, height);
                break;
            case ERROR:
                theme.drawElement(surface, ThemeItem.EDITBOX_ERROR, x, y, width, height);
                break;
            case OK:
                theme.drawElement(surface, ThemeItem.EDITBOX_OK, x, y, width, height);
                break;
        }
    }

    private IFill getEditBoxTextColor(EditBoxState state) {
        switch (state) {
            case ERROR:
                return Color.RED;
            case OK:
                return Color.GREEN;
            default:
                return Color.WHITE;
        }
    }

    public void drawEditBox(Surface surface, String text, int x, int y, int width, int height, EditBoxState state) {
        IFill textColor = getEditBoxTextColor(state);

        drawEditBoxFrame(surface, x, y, width, height, state);

        drawAlignedText(surface, text, x + 6, y, width - 12, height, textColor, Alignment.LEFT);
    }
}

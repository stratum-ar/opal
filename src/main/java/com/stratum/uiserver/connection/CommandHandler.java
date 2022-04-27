package com.stratum.uiserver.connection;

import com.stratum.uiserver.UIServerApp;
import com.stratum.uiserver.annotation.RequestMethod;
import com.stratum.uiserver.graphics.Graphics;
import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.font.BitmapFont;
import com.stratum.uiserver.graphics.icons.IconSet;
import com.stratum.uiserver.graphics.icons.Icons;
import com.stratum.uiserver.graphics.types.Color;
import com.stratum.uiserver.graphics.types.IFill;
import com.stratum.uiserver.graphics.ui.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class CommandHandler {
    private final Graphics graphics;
    private final Surface surface;
    private final UIPrimitives uiPrimitives;
    IconSet iconSet;
    BitmapFont font;

    public CommandHandler(Surface surface, UIPrimitives uiPrimitives) throws IOException, URISyntaxException {
        this.surface = surface;
        this.graphics = surface.getGraphics();
        this.uiPrimitives = uiPrimitives;
        this.iconSet = IconSet.load(UIServerApp.class.getResourceAsStream("/default_icons.bin"));
        this.font = BitmapFont.load(UIServerApp.class.getResourceAsStream("/default_font.bin"));
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

    @RequestMethod(commandNo = 22)
    public void drawIcon(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int iconNo = in.readUnsignedByte();

        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        iconSet.drawIcon(surface, Icons.values()[iconNo], x, y, fill);
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

    @RequestMethod(commandNo = 30)
    public void drawText(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int stringLen = in.readUnsignedByte();
        String string = new String(in.readNBytes(stringLen), StandardCharsets.UTF_8);

        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        font.drawText(surface, string, x, y, fill);
    }

    @RequestMethod(commandNo = 32)
    public void drawAlignedText(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        int alignmentNo = in.readUnsignedByte();

        int stringLen = in.readUnsignedByte();
        String string = new String(in.readNBytes(stringLen), StandardCharsets.UTF_8);

        float r = in.readUnsignedByte() / 255f;
        float g = in.readUnsignedByte() / 255f;
        float b = in.readUnsignedByte() / 255f;
        IFill fill = new Color(r, g, b);

        uiPrimitives.drawAlignedText(surface, string, x, y, width, height, fill, Alignment.values()[alignmentNo]);
    }

    @RequestMethod(commandNo = 33)
    public void drawButton(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        int buttonState = in.readUnsignedByte();

        int stringLen = in.readUnsignedByte();
        String string = new String(in.readNBytes(stringLen), StandardCharsets.UTF_8);

        Icons icon = in.readUnsignedByte() == 1? Icons.values()[in.readUnsignedByte()] : null;
        
        uiPrimitives.drawButton(surface, string, icon, x, y, width, height, ButtonState.values()[buttonState]);
    }

    @RequestMethod(commandNo = 34)
    public void drawComboBox(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        int buttonState = in.readUnsignedByte();

        int stringLen = in.readUnsignedByte();
        String string = new String(in.readNBytes(stringLen), StandardCharsets.UTF_8);

        uiPrimitives.drawComboBox(surface, string, x, y, width, height, ButtonState.values()[buttonState]);
    }

    @RequestMethod(commandNo = 35)
    public void drawCheckBox(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        boolean isChecked = in.readUnsignedByte() == 1;

        uiPrimitives.drawCheckbox(surface, x, y, isChecked);
    }

    @RequestMethod(commandNo = 37)
    public void drawSlider(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        double fillPercentage = (double) in.readUnsignedByte() / 255;
        boolean vertical = in.readUnsignedByte() == 1;

        if (vertical)
            uiPrimitives.drawVerticalSlider(surface, fillPercentage, x, y, width, height);
        else
            uiPrimitives.drawHorizontalSlider(surface, fillPercentage, x, y, width, height);
    }

    @RequestMethod(commandNo = 38)
    public void drawProgress(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        double progress = (double) in.readUnsignedByte() / 255;

        uiPrimitives.drawProgress(surface, progress, x, y, width, height);
    }

    @RequestMethod(commandNo = 39)
    public void drawEditBox(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        EditBoxState state = EditBoxState.values()[in.readUnsignedByte()];

        int stringLen = in.readUnsignedByte();
        String string = new String(in.readNBytes(stringLen), StandardCharsets.UTF_8);

        boolean isSelected = in.readUnsignedByte() == 1;

        TextSelection selection = isSelected ? new TextSelection(in.readUnsignedByte(), in.readUnsignedByte()) : null;

        uiPrimitives. drawEditBox(surface, string, x, y, width, height, state, selection);
    }

    @RequestMethod(commandNo = 43)
    public void drawPager(DataInputStream in) throws IOException {
        int x = in.readUnsignedByte();
        int y = in.readUnsignedByte();
        int width = in.readUnsignedByte();
        int height = in.readUnsignedByte();
        int noOfItems = in.readUnsignedByte();
        int selectedIndex = in.readUnsignedByte();
        boolean vertical = in.readUnsignedByte() == 1;

        uiPrimitives.drawPager(surface, noOfItems, selectedIndex, x, y, width, height, vertical);
    }

}

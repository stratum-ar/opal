package com.stratum.uiserver.connection;

import com.stratum.uiserver.UIServerApp;
import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.font.BitmapFont;
import com.stratum.uiserver.graphics.icons.IconSet;
import com.stratum.uiserver.graphics.theme.Theme;
import com.stratum.uiserver.graphics.ui.UIPrimitives;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class RequestReader {

    Theme theme = Theme.load(UIServerApp.class.getResourceAsStream("/default_theme.bin"));
    IconSet iconSet = IconSet.load(UIServerApp.class.getResourceAsStream("/default_icons.bin"));
    BitmapFont font = BitmapFont.load(UIServerApp.class.getResourceAsStream("/default_font.bin"));

    private final DataInputStream in;
    private final Surface surface = new Surface();
    private final UIPrimitives uiPrimitives = new UIPrimitives(theme, iconSet, font);

    public RequestReader(DataInputStream in) throws IOException {
        this.in = in;
    }

    public Surface readRequest() {
        try {
            int commandsNum = getNumberOfCommands();

            for (int i = 0; i < commandsNum; i++) {
                readCommand();

                int divider = in.readUnsignedByte();

                if (divider == 2)
                    break;
                else if (divider == 1)
                    continue;
                else throw new IOException();
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Wrong input stream");
        }
        return surface;
    }

    public int getNumberOfCommands() throws IOException {
        return in.readUnsignedByte();
    }

    public void readCommand() throws IOException {
        try {
            CommandHandler commandHandler = new CommandHandler(surface, uiPrimitives);
            int commandNo = in.readUnsignedByte();

            commandHandler.runCommand(commandNo, in);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}

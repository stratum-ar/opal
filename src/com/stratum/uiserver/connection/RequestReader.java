package com.stratum.uiserver.connection;

import com.stratum.uiserver.graphics.ColorUtil;
import com.stratum.uiserver.graphics.Graphics;
import com.stratum.uiserver.graphics.Surface;

import java.io.DataInputStream;
import java.io.IOException;

public class RequestReader {

    private final DataInputStream in;
    private final Surface surface = new Surface();

    public RequestReader(DataInputStream in) {
        this.in = in;
    }

    public Surface readRequest() {
        try {
            int commandsNum = getNumberOfCommands();
            int commandSectionLength = getCommandSectionLength();

            for (int i = 0; i < commandsNum; i++) {
                getCommand();
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Wrong input stream");
        }
        return surface;
    }

    public int getNumberOfCommands() throws IOException {
        return in.readByte();
    }

    public int getCommandSectionLength() throws IOException {
        return (((in.readByte() & 0xff) << 8) | in.readByte() & 0xff);
    }

    public void getCommand() throws IOException {
        int command = in.readByte();
        switch (command) {
            case 18:
                readCommand(command, in.readNBytes(8));
        }
    }

    public Surface readCommand(int command, byte[] parameters) {
        Graphics g = surface.getGraphics();
        switch (command) {
            case 18:
                g.fillRect(
                        parameters[0],
                        parameters[1],
                        parameters[2],
                        parameters[3],
                        ColorUtil.pack(parameters[4], parameters[5], parameters[6]));
        }
        return surface;
    }


}

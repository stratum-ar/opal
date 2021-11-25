package com.stratum.uiserver.connection;

import com.stratum.uiserver.graphics.Surface;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class RequestReader {

    private final DataInputStream in;
    private final Surface surface = new Surface();

    public RequestReader(DataInputStream in) {
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
            CommandHandler commandHandler = new CommandHandler(surface);
            int commandNo = in.readUnsignedByte();

            commandHandler.runCommand(commandNo, in);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}

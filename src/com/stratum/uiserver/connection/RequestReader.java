package com.stratum.uiserver.connection;

import com.stratum.uiserver.annotation.RequestMethod;
import com.stratum.uiserver.graphics.Graphics;
import com.stratum.uiserver.graphics.Surface;
import com.stratum.uiserver.graphics.types.Color;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

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

    public int getCommandSectionLength() throws IOException {
        return (((in.readUnsignedByte() & 0xff) << 8) | in.readUnsignedByte() & 0xff);
    }

    public void getCommand() throws IOException {
        int command = in.readUnsignedByte();
        switch (command) {
            case 18:
                readCommand(command, in.readNBytes(8));
        }
    }

    public void readCommand() throws IOException {
        Graphics g = surface.getGraphics();
        int commandNo = in.readUnsignedByte();

        runCommand(commandNo, g, in);

    }

    public void readCommand(int command, byte[] parameters) {
        Graphics g = surface.getGraphics();
        switch (command) {
            case 18:
                g.fillRect(
                        parameters[0],
                        parameters[1],
                        parameters[2],
                        parameters[3],
                        new Color((float) (parameters[4]/255.0), (float) (parameters[5]/255.0), (float) (parameters[6]/255.0)));
        }
    }

    public void runCommand(int commandNo, Graphics g, DataInputStream in) {
        Method[] methods = Graphics.class.getMethods();
        try {
            for (Method method : methods) {

                if (((AnnotatedElement) method).isAnnotationPresent(RequestMethod.class)) {
                    RequestMethod requestMethod = ((AnnotatedElement) method).getAnnotation(RequestMethod.class);

                    if (commandNo == requestMethod.commandNo()) {
                        method.invoke(g, in);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

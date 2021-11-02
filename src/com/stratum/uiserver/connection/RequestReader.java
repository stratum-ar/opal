package com.stratum.uiserver.connection;

import com.google.common.primitives.Bytes;
import com.stratum.uiserver.graphics.ColorUtil;
import com.stratum.uiserver.graphics.Graphics;
import com.stratum.uiserver.graphics.Surface;

import java.util.List;

public class RequestReader {

    private final List<Byte> requestList;
    private final Surface surface = new Surface();

    public RequestReader(byte[] request) {
        requestList = Bytes.asList(request);
    }

    public Surface readRequest() {
        return readCommand(getCommand());
    }

    public int getNumberOfCommands() {
        return requestList.get(0);
    }

    public int getCommandSectionLength() {
        return ((requestList.get(1) & 0xff) << 8) | (requestList.get(2) & 0xff);
    }

    public List<Byte> getCommand() {
        return requestList.subList(3, 3 + 8);
    }

    public Surface readCommand(List<Byte> command) {
        Graphics g = surface.getGraphics();
        switch (command.get(0)) {
            case 18:
                g.fillRect(
                        command.get(1),
                        command.get(2),
                        command.get(3),
                        command.get(4),
                        ColorUtil.pack((float)command.get(5), (float)command.get(6), (float)command.get(7)));
        }
        return surface;
    }


}

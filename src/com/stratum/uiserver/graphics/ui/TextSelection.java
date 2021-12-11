package com.stratum.uiserver.graphics.ui;

public class TextSelection {
    private int start;
    private int length;

    public TextSelection(int start, int length) {
        this.start = start;
        this.length = length;
    }

    public int getStartPosition() {
        return Math.max(0, start);
    }

    public int getStartPosition(int stringLength) {
        if (start < 0) {
            return stringLength + start + 1;
        }
        return start;
    }

    public int getLength() {
        return length;
    }

    public void set(int start, int length) {
        this.start = start;
        this.length = length;
    }
}

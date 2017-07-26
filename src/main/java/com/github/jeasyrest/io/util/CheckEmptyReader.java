package com.github.jeasyrest.io.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class CheckEmptyReader extends FilterReader {

    public CheckEmptyReader(Reader in) {
        super(in);
        if (!(in instanceof CheckEmptyReader)) {
            first = new char[1];
        }
    }

    @Override
    public int read() throws IOException {
        char[] cbuf = new char[1];
        if (read(cbuf) == -1) {
            return -1;
        } else {
            return cbuf[0];
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (first == null || first.length == 0) {
            return super.read(cbuf, off, len);
        } else {
            cbuf[off] = first[0];
            first = new char[0];
            int rl = super.read(cbuf, off + 1, len - 1);
            if (rl == -1) {
                return -1;
            } else {
                return rl + 1;
            }
        }
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    public boolean isEmpty() throws IOException {
        if (first == null) {
            return ((CheckEmptyReader) in).isEmpty();
        } else {
            first[0] = (char) super.read();
            return first[0] == ((char) -1);
        }
    }

    private char[] first;

}

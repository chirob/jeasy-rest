package com.github.jeasyrest.io.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class AutoclosingReader extends FilterReader {

    public AutoclosingReader(Reader in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        return checkEOF(super.read());
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return checkEOF(super.read(cbuf, off, len));
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        return checkEOF(super.read(cbuf));
    }

    private int checkEOF(int rl) throws IOException {
        if (rl == -1) {
            close();
        }
        return rl;
    }

}

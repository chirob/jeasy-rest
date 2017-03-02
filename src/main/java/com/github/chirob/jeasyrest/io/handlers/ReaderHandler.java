package com.github.chirob.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.chirob.jeasyrest.io.StreamHandler;

public class ReaderHandler implements StreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) {
        reader = (Reader) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        return reader;
    }

    @Override
    public Writer getWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    private Reader reader;

}

package com.github.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.io.StreamHandler;

public class WriterHandler implements StreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) {
        writer = (Writer) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer getWriter() throws IOException {
        return writer;
    }

    private Writer writer;

}

package com.github.chirob.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

public class InputStreamHandler extends BinaryStreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(sourceObject, sourceParams);
        inputStream = (InputStream) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(inputStream, getCharset());
    }

    @Override
    public Writer getWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    private InputStream inputStream;

}

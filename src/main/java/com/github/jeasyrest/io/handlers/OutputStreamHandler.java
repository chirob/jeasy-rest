package com.github.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class OutputStreamHandler extends BinaryStreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(sourceObject, sourceParams);
        outputStream = (OutputStream) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(outputStream, getCharset());
    }

    private OutputStream outputStream;

}

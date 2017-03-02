package com.github.chirob.jeasyrest.io.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class FileHandler extends BinaryStreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(sourceObject, sourceParams);
        file = (File) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(new FileInputStream(file), getCharset());
    }

    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(new FileOutputStream(file), getCharset());
    }

    private File file;

}

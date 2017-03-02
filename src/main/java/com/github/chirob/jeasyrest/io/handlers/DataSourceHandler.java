package com.github.chirob.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.activation.DataSource;

public class DataSourceHandler extends BinaryStreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(sourceObject, sourceParams);
        dataSource = (DataSource) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(dataSource.getInputStream(), getCharset());
    }

    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(dataSource.getOutputStream(), getCharset());
    }

    private DataSource dataSource;

}

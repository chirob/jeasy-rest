package com.github.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobHandler extends BinaryStreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) {
        blob = (Blob) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        try {
            return new InputStreamReader(blob.getBinaryStream(), getCharset());
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Writer getWriter() throws IOException {
        try {
            return new OutputStreamWriter(blob.setBinaryStream(1), getCharset());
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private Blob blob;

}

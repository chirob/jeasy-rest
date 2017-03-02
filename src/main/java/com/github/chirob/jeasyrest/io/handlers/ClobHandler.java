package com.github.chirob.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

import com.github.chirob.jeasyrest.io.StreamHandler;

public class ClobHandler implements StreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) {
        clob = (Clob) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        try {
            return clob.getCharacterStream();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Writer getWriter() throws IOException {
        try {
            return clob.setCharacterStream(1);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private Clob clob;

}

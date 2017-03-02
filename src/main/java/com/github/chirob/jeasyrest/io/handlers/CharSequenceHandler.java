package com.github.chirob.jeasyrest.io.handlers;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import com.github.chirob.jeasyrest.io.StreamHandler;

public class CharSequenceHandler implements StreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        String text = ((CharSequence) sourceObject).toString();
        try {
            URL url = new URL(text);
            handler = new URLHandler();
            handler.init(url, sourceParams);
        } catch (Exception e) {
            try {
                File file = new File(text);
                if (file.exists()) {
                    handler = new FileHandler();
                    handler.init(file, sourceParams);
                }
            } catch (Exception ex) {
            }
        }
        if (handler == null) {
            this.text = text;
        }
    }

    @Override
    public Reader getReader() throws IOException {
        if (handler == null) {
            return new StringReader(text);
        } else {
            return handler.getReader();
        }
    }

    @Override
    public Writer getWriter() throws IOException {
        if (handler == null) {
            throw new UnsupportedOperationException();
        } else {
            return handler.getWriter();
        }
    }

    private StreamHandler handler;
    private String text;

}

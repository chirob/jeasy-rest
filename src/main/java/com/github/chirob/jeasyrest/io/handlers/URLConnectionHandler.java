package com.github.chirob.jeasyrest.io.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URLConnection;

public class URLConnectionHandler extends BinaryStreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(sourceObject, sourceParams);
        urlConnection = (URLConnection) sourceObject;
        urlConnection.setUseCaches(false);
    }

    @Override
    public Reader getReader() throws IOException {
        urlConnection.setDoInput(true);
        InputStream inputStream = urlConnection.getInputStream();
        return new InputStreamReader(inputStream, getCharset());
    }

    @Override
    public Writer getWriter() throws IOException {
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        return new OutputStreamWriter(outputStream, getCharset());
    }

    private URLConnection urlConnection;

}

package com.github.chirob.jeasyrest.core.http;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.github.chirob.jeasyrest.core.io.Channel;

public class RemoteChannel implements Channel {

    @Override
    public void close() throws IOException {
        try {
            connection.disconnect();
        } finally {
            connection = null;
        }
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(getConnection().getInputStream(), charset) {
            @Override
            public void close() throws IOException {
                try {
                    super.close();
                } finally {
                    RemoteChannel.this.close();
                }
            }
        };
    }

    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(getConnection().getOutputStream(), charset);
    }

    @Override
    public boolean isClosed() throws IOException {
        return false;
    }

    public RemoteChannel(String remotePath, String encoding) throws IOException {
        remoteURL = new URL(remotePath);
        charset = Charset.forName(encoding);
    }

    private HttpURLConnection getConnection() throws IOException {
        if (connection == null) {
            connection = (HttpURLConnection) remoteURL.openConnection();
            connection.connect();
        }
        return connection;
    }

    private URL remoteURL;
    private Charset charset;

    private HttpURLConnection connection;

}

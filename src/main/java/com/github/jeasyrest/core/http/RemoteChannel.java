package com.github.jeasyrest.core.http;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

import com.github.jeasyrest.core.io.Channel;

public class RemoteChannel implements Channel {

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.disconnect();
            } finally {
                connection = null;
            }
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

    public RemoteChannel(URI remoteURI, String encoding) throws IOException {
        remoteURL = remoteURI.toURL();
        charset = Charset.forName(encoding);
    }

    private HttpURLConnection getConnection() throws IOException {
        if (connection == null) {
            connection = (HttpURLConnection) remoteURL.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            HttpRunningContext.init(connection);
        }
        return connection;
    }

    private URL remoteURL;
    private Charset charset;

    private HttpURLConnection connection;

}

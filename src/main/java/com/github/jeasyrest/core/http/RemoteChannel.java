package com.github.jeasyrest.core.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import javax.activation.MimeType;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IHeaders;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.io.util.IOUtils;

public class RemoteChannel implements IChannel {

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                getConnection().disconnect();
            } finally {
                connection = null;
            }
        }
    }

    @Override
    public Reader getReader() throws IOException {
        InputStream inStream = null;
        try {
            inStream = getConnection().getInputStream();
        } catch (IOException e) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                IOUtils.write(getConnection().getErrorStream(), true, baos, false);
            } catch (Exception ex) {
            }
            throw new IOException(baos.toString("UTF-8"), e);
        }
        return new InputStreamReader(inStream, charset) {
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

    @Override
    public IHeaders requestHeaders() throws IOException {
        return new RemoteHeaders(true, getConnection());
    }

    @Override
    public IHeaders responseHeaders() throws IOException {
        return new RemoteHeaders(false, getConnection());
    }

    public HttpURLConnection getConnection() throws IOException {
        if (connection == null) {
            connection = new RemoteConnection(resource);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(method.name());
        }
        return connection;
    }

    public void setContentType(String contentType) throws IOException {
        getConnection().setRequestProperty("Content-Type", contentType.toString());
    }

    public String getContentType() throws IOException {
        return getConnection().getRequestProperty("Content-Type");
    }

    public RemoteChannel(RemoteResource<?, ?> resource, Method method) throws IOException {
        this.method = method;
        this.resource = resource;
        charset = Charset.forName("UTF-8");
    }

    protected void setContentMimeType(MimeType contentType) {
        String encoding = null;
        if (contentType != null) {
            encoding = contentType.getParameter("charset");
        }
        if (encoding != null) {
            charset = Charset.forName(encoding);
        }
    }

    private RemoteResource<?, ?> resource;
    private Method method;
    private Charset charset;

    private HttpURLConnection connection;

}

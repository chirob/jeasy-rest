package com.github.jeasyrest.core.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IHeaders;

public class WrapperChannel implements IChannel {

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public IHeaders requestHeaders() throws IOException {
        return channel.requestHeaders();
    }

    @Override
    public IHeaders responseHeaders() throws IOException {
        return channel.responseHeaders();
    }

    @Override
    public Reader getReader() throws IOException {
        return channel.getReader();
    }

    @Override
    public Writer getWriter() throws IOException {
        return channel.getWriter();
    }

    @Override
    public boolean isClosed() throws IOException {
        return channel.isClosed();
    }

    public IChannel unwrap() {
        return channel;
    }
    
    protected WrapperChannel(IChannel channel) {
        this.channel = channel;
    }

    private IChannel channel;

}

package com.github.chirob.jeasyrest.core.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class WrapperChannel implements Channel {

    @Override
    public void close() throws IOException {
        channel.close();
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

    protected WrapperChannel(Channel channel) {
        this.channel = channel;
    }

    private Channel channel;
}

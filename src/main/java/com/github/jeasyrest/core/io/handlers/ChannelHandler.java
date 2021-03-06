package com.github.jeasyrest.core.io.handlers;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.io.StreamHandler;

public class ChannelHandler implements StreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        channel = (IChannel) sourceObject;
    }

    @Override
    public Reader getReader() throws IOException {
        return channel.getReader();
    }

    @Override
    public Writer getWriter() throws IOException {
        return channel.getWriter();
    }

    private IChannel channel;

}

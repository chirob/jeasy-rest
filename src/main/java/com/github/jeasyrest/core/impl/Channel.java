package com.github.jeasyrest.core.impl;

import java.io.IOException;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IHeaders;

public abstract class Channel implements IChannel {

    @Override
    public IHeaders requestHeaders() throws IOException {
        return new Headers(true);
    }

    @Override
    public IHeaders responseHeaders() throws IOException {
        return new Headers(false);
    }

}

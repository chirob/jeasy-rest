package com.github.jeasyrest.core.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IHeaders;
import com.github.jeasyrest.core.IRunningContext;

public abstract class Channel implements IChannel {

    @Override
    public IHeaders requestHeaders() throws IOException {
        HttpServletRequest request = IRunningContext.INSTANCE.getInstance(HttpServletRequest.class);
        return request == null ? null : new Headers(request);
    }

    @Override
    public IHeaders responseHeaders() throws IOException {
        HttpServletResponse response = IRunningContext.INSTANCE.getInstance(HttpServletResponse.class);
        return response == null ? null : new Headers(response);
    }

}

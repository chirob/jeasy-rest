package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.io.UnavailableStreamException;
import com.github.jeasyrest.core.io.WrapperChannel;
import com.github.jeasyrest.io.util.AutoclosingReader;
import com.github.jeasyrest.io.util.IOUtils;

class ResourceChannel extends WrapperChannel {

    @Override
    public Reader getReader() throws IOException {
        IOUtils.close(writer);

        Reader reader = null;
        try {
            reader = super.getReader();
            if (reader != null) {
                if (!(reader instanceof AutoclosingReader)) {
                    reader = new AutoclosingReader(reader);
                }
            }
            return reader;
        } catch (Exception e) {
            IOUtils.close(reader);
            throw new UnavailableStreamException(e);
        }
    }

    @Override
    public Writer getWriter() throws IOException {
        try {
            return writer = super.getWriter();
        } catch (Exception e) {
            IOUtils.close(writer);
            throw new UnavailableStreamException(e);
        }
    }

    public Method getMethod() {
        return method;
    }

    ResourceChannel(IChannel channel, Method method) {
        super(channel);
        this.method = method;
    }

    private Writer writer;
    private Method method;

}

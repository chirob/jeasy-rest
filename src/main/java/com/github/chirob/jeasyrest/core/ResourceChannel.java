package com.github.chirob.jeasyrest.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.core.io.UnavailableStreamException;
import com.github.chirob.jeasyrest.core.io.WrapperChannel;
import com.github.chirob.jeasyrest.io.util.AutoclosingReader;
import com.github.chirob.jeasyrest.io.util.IOUtils;

class ResourceChannel extends WrapperChannel {

    @Override
    public Reader getReader() throws IOException {
        IOUtils.close(writer);

        try {
            Reader reader = super.getReader();
            if (reader != null) {
                if (!(reader instanceof AutoclosingReader)) {
                    reader = new AutoclosingReader(reader);
                }
            }
            return reader;
        } catch (Exception e) {
            throw new UnavailableStreamException(e);
        }
    }

    @Override
    public Writer getWriter() throws IOException {
        try {
            return writer = super.getWriter();
        } catch (Exception e) {
            throw new UnavailableStreamException(e);
        }
    }

    ResourceChannel(Channel channel) {
        super(channel);
    }

    private Writer writer;

}

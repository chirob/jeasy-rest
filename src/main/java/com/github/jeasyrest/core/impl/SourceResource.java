package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.Resource;
import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.io.Source;
import com.github.jeasyrest.io.util.IOUtils;

public class SourceResource extends Resource {

    public SourceResource(Object sourceObject) {
        this.sourceObject = sourceObject;
    }

    @Override
    public Channel openChannel(Method method) throws IOException {
        if (sourceObject instanceof Resource) {
            return ((Resource) sourceObject).openChannel(method);
        } else {
            return new SourceChannel(sourceObject);
        }
    }

    private static final class SourceChannel implements Channel {
        @Override
        public void close() {
            IOUtils.close(reader, writer);
            closed = true;
        }

        @Override
        public Reader getReader() throws IOException {
            return reader = source.getReader();
        }

        @Override
        public Writer getWriter() throws IOException {
            return writer = source.getWriter();
        }

        @Override
        public boolean isClosed() {
            return closed;
        }

        private SourceChannel(Object sourceObject) throws IOException {
            source = new Source(sourceObject);
        }

        private boolean closed;
        private Source source;
        private Reader reader;
        private Writer writer;
    }

    private Object sourceObject;

}

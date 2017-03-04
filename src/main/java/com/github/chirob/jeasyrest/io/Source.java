package com.github.chirob.jeasyrest.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.github.chirob.jeasyrest.io.util.IOUtils;

public class Source {

    public Source(Object sourceObject, Object... sourceParams) throws IOException {
        init(sourceObject, sourceParams);
    }

    public void writeTo(Object sourceObject, Object... sourceParams) throws IOException {
        writeTo(new Source(sourceObject, sourceParams));
    }

    public void writeTo(Source target) throws IOException {
        IOUtils.write(getReader(), true, target.getWriter(), true);
    }

    public Reader getReader() throws IOException {
        Exception e = null;
        try {
            for (StreamHandler sh : streamHandlers) {
                return sh.getReader();
            }
        } catch (Exception ex) {
            e = ex;
        }
        return IOUtils.throwIOException(e);
    }

    public Writer getWriter() throws IOException {
        Exception e = null;
        try {
            for (StreamHandler sh : streamHandlers) {
                return sh.getWriter();
            }
        } catch (Exception ex) {
            e = ex;
        }
        return IOUtils.throwIOException(e);
    }

    protected Source() {
    }

    protected void init(Object sourceObject, Object... sourceParams) throws IOException {
        streamHandlers = HANDLER_MAP.get(sourceObject.getClass());
        for (StreamHandler streamHandler : streamHandlers) {
            try {
                streamHandler.init(sourceObject, sourceParams);
            } catch (Exception e) {
            }
        }
        if (streamHandlers.isEmpty()) {
            throw new IllegalArgumentException("No stream handler found for type: " + sourceObject.getClass());
        }
    }

    private List<? extends StreamHandler> streamHandlers;

    private static final StreamHandlerMap HANDLER_MAP = new StreamHandlerMap();

}

package com.github.jeasyrest.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import com.github.jeasyrest.io.util.IOUtils;

public class Source {

    public Source(Object sourceObject, Object... sourceParams) throws IOException {
        init(sourceObject, sourceParams);
    }

    public void writeTo(Source target, Object... sourceParams) throws IOException {
        writeTo(true, target, sourceParams);
    }

    public void writeTo(boolean closeTarget, Source target, Object... sourceParams) throws IOException {
        Reader reader = getReader();
        Writer writer = target.getWriter();
        if (reader != null && writer != null) {
            IOUtils.write(reader, true, writer, closeTarget);
        }
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
        Exception exception = null;
        streamHandlers = HANDLER_MAP.get(sourceObject.getClass());
        for (Iterator<? extends StreamHandler> it = streamHandlers.iterator(); it.hasNext();) {
            try {
                it.next().init(sourceObject, sourceParams);
            } catch (Exception e) {
                it.remove();
                exception = e;
            }
        }
        if (streamHandlers.isEmpty()) {
            throw new IllegalArgumentException("No stream handler found for type: " + sourceObject.getClass(),
                    exception);
        }
    }

    private List<? extends StreamHandler> streamHandlers;

    private static final StreamHandlerMap HANDLER_MAP = new StreamHandlerMap();

}
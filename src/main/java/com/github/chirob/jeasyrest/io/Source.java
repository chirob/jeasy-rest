package com.github.chirob.jeasyrest.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.chirob.jeasyrest.io.util.IOUtils;
import com.github.chirob.jeasyrest.reflect.InstanceConstructor;

public class Source {

    public Source(Object sourceObject, Object... sourceParams) throws IOException {
        HANDLER_MAP.getNewInstance(sourceObject.getClass().getName());
        Set<InstanceConstructor<? extends StreamHandler>> streamHandlerConstructors = HANDLER_MAP
                .get(sourceObject.getClass());
        if (streamHandlerConstructors.isEmpty()) {
            throw new IllegalArgumentException("No stream handler found for type: " + sourceObject.getClass());
        }
        for (InstanceConstructor<? extends StreamHandler> streamHandlerConstructor : streamHandlerConstructors) {
            try {
                StreamHandler streamHandler = streamHandlerConstructor.newInstance();
                streamHandler.init(sourceObject, sourceParams);
                streamHandlers.add(streamHandler);
            } catch (Exception e) {
            }
        }
        if (streamHandlers.isEmpty()) {
            throw new IllegalArgumentException("No stream handler instance can be created");
        }
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

    private List<StreamHandler> streamHandlers = new LinkedList<StreamHandler>();

    private static final StreamHandlerMap HANDLER_MAP = new StreamHandlerMap();

}

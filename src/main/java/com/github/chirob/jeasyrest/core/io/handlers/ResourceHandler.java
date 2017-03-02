package com.github.chirob.jeasyrest.core.io.handlers;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;
import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.io.handlers.BinaryStreamHandler;

public class ResourceHandler extends BinaryStreamHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(sourceObject, sourceParams);
        Resource resource = (Resource) sourceObject;
        Method method = getMethod(sourceParams);
        resourceChannel = resource.getChannel(method);
    }

    @Override
    public Reader getReader() throws IOException {
        return resourceChannel.getReader();
    }

    @Override
    public Writer getWriter() throws IOException {
        return resourceChannel.getWriter();
    }

    private static Method getMethod(Object... sourceParams) {
        for (Object param : sourceParams) {
            if (param instanceof Resource.Method) {
                return (Method) param;
            } else {
                try {
                    return Method.valueOf(param.toString().toUpperCase());
                } catch (Exception e) {
                }
            }
        }
        throw new IllegalArgumentException("No valid REST method has been specified");
    }

    private Channel resourceChannel;

}

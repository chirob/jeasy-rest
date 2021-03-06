package com.github.jeasyrest.core.io.handlers;

import java.io.IOException;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResource.Method;

public class ResourceHandler extends ChannelHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(((IResource) sourceObject).getChannel(getMethod(sourceParams)));
    }

    private static Method getMethod(Object... sourceParams) {
        for (Object param : sourceParams) {
            if (param instanceof Method) {
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

}
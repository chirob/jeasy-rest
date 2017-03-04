package com.github.chirob.jeasyrest.core.io.handlers;

import java.io.IOException;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;

public class ResourceHandler extends ChannelHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(((Resource) sourceObject).getChannel(getMethod(sourceParams)), sourceParams);
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

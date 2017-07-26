package com.github.jeasyrest.core;

import java.io.IOException;
import java.net.URI;

public interface IResource {

    enum Method {
        DELETE, GET, OPTIONS, PATCH, POST, PUT
    }

    IChannel getChannel(Method method) throws IOException;

    IChannel getChannel(String methodName) throws IOException;

    IChannel delete() throws IOException;

    IChannel get() throws IOException;

    IChannel options() throws IOException;

    IChannel patch() throws IOException;

    IChannel post() throws IOException;

    IChannel put() throws IOException;

    String[] getParameters();

    void setParameter(int index, String paramValue);

    URI getPath();

    String getPathPattern();

}

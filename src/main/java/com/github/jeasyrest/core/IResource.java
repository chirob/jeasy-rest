package com.github.jeasyrest.core;

import java.io.IOException;
import java.net.URI;

import com.github.jeasyrest.core.io.Channel;

public interface IResource {

    enum Method {
        DELETE, GET, OPTIONS, PATCH, POST, PUT
    }

    Channel getChannel(Method method) throws IOException;

    Channel getChannel(String methodName) throws IOException;

    Channel delete() throws IOException;

    Channel get() throws IOException;

    Channel options() throws IOException;

    Channel patch() throws IOException;

    Channel post() throws IOException;

    Channel put() throws IOException;

    String[] getParameters();

    void setParameter(int index, String paramValue);

    URI getPath();

    String getPathPattern();

}

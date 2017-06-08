package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.core.security.ResourcePolicy;

public abstract class Resource implements IResource {

    public abstract Channel openChannel(Method method) throws IOException;

    public final Channel getChannel(Method method) throws IOException {
        ResourcePolicy.checkPermission(pathPattern, Arrays.asList(method));
        if (channel == null || channel.isClosed()) {
            channel = new ResourceChannel(openChannel(method));
        }
        return channel;
    }

    public final Channel getChannel(String methodName) throws IOException {
        return getChannel(Method.valueOf(methodName.toUpperCase()));
    }

    public final Channel delete() throws IOException {
        return getChannel(Method.DELETE);
    }

    public final Channel get() throws IOException {
        return getChannel(Method.GET);
    }

    public final Channel options() throws IOException {
        return getChannel(Method.OPTIONS);
    }

    public final Channel patch() throws IOException {
        return getChannel(Method.PATCH);
    }

    public final Channel post() throws IOException {
        return getChannel(Method.POST);
    }

    public final Channel put() throws IOException {
        return getChannel(Method.PUT);
    }

    public final String[] getParameters() {
        return parameters;
    }

    public final void setParameter(int index, String paramValue) {
        parameters[index] = paramValue;
    }

    public final URI getPath() {
        return path;
    }

    public final String getPathPattern() {
        return pathPattern;
    }

    void init(URI path, String pathPattern, Object[] parameters) {
        ResourcePolicy.checkPermission(pathPattern);

        this.path = path;
        this.pathPattern = pathPattern;
        this.parameters = parameters == null ? new String[0]
                : Arrays.copyOf(parameters, parameters.length, String[].class);
    }

    URI path;
    String pathPattern;
    String[] parameters;

    protected Channel channel;

}

package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.security.ResourcePolicy;

public abstract class Resource implements IResource {

    public abstract IChannel openChannel(Method method) throws IOException;

    public final IChannel getChannel(Method method) throws IOException {
        ResourcePolicy.checkPermission(pathPattern, Arrays.asList(method));
        if (channel == null || channel.isClosed()) {
            channel = new ResourceChannel(openChannel(method));
        }
        return channel;
    }

    public final IChannel getChannel(String methodName) throws IOException {
        return getChannel(Method.valueOf(methodName.toUpperCase()));
    }

    public final IChannel delete() throws IOException {
        return getChannel(Method.DELETE);
    }

    public final IChannel get() throws IOException {
        return getChannel(Method.GET);
    }

    public final IChannel options() throws IOException {
        return getChannel(Method.OPTIONS);
    }

    public final IChannel patch() throws IOException {
        return getChannel(Method.PATCH);
    }

    public final IChannel post() throws IOException {
        return getChannel(Method.POST);
    }

    public final IChannel put() throws IOException {
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

    protected IChannel currentChannel() {
        return channel;
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

    private IChannel channel;

}

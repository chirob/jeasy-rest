package com.github.chirob.jeasyrest.core;

import java.io.IOException;
import java.net.URI;
import java.security.AccessController;

import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.core.security.ResourcePermission;

public abstract class Resource {

    public enum Method {
        DELETE, GET, OPTIONS, POST, PUT
    }

    public static final Resource getResource(String resourcePath) {
        Resource resource = RESOURCE_MAP.get(resourcePath);
        AccessController.checkPermission(new ResourcePermission(resource));
        return resource;
    }

    public abstract Channel openChannel(Method method) throws IOException;

    public final Channel getChannel(Method method) throws IOException {
        AccessController.checkPermission(new ResourcePermission(this, method));
        return openChannel(method);
    }

    public final Channel getChannel(String method) throws IOException {
        return getChannel(Method.valueOf(method.toUpperCase()));
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

    URI path;
    String[] parameters;

    private static final ResourceMap RESOURCE_MAP = new ResourceMap();
}

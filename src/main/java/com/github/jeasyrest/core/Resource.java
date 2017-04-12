package com.github.jeasyrest.core;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.core.io.WrapperChannel;
import com.github.jeasyrest.core.security.ResourcePolicy;
import com.github.jeasyrest.ioc.Injections;
import com.github.jeasyrest.ioc.util.PooledInstance;

public abstract class Resource {

    public enum Method {
        DELETE, GET, OPTIONS, PATCH, POST, PUT
    }

    @SuppressWarnings("unchecked")
    public static final <T extends Resource> T getResource(String resourcePath) {
        T resource = RESOURCE_MAP.get(resourcePath);
        if (resource == null) {
            final PooledInstance<Resource> pooledRemoteResource = Injections.INSTANCE.pooledInstance("remoteResource");
            Resource remoteResource = pooledRemoteResource.pop(resourcePath);
            resource = (T) new ResourceWrapper(remoteResource) {
                @Override
                public Channel openChannel(Method method) throws IOException {
                    return new WrapperChannel(super.openChannel(method)) {
                        @Override
                        public void close() throws IOException {
                            try {
                                super.close();
                            } finally {
                                pooledRemoteResource.release();
                            }
                        }
                    };
                }
            };
        }
        return resource;
    }

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
        this.parameters = Arrays.copyOf(parameters, parameters.length, String[].class);
    }

    URI path;
    String pathPattern;
    String[] parameters;

    private Channel channel;

    private static final ResourceMap RESOURCE_MAP = new ResourceMap();

}
package com.github.jeasyrest.core.client;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.io.Channel;

public abstract class ResourceHandler<Req, Res> {

    protected abstract void marshall(Req request, Writer writer) throws IOException;

    protected abstract Res unmarshall(Class<Res> responseType, Reader reader) throws IOException;

    public ResourceHandler(String resourcePath) {
        this(IResourceFinder.INSTANCE.find(resourcePath));
    }

    public ResourceHandler(IResource resource) {
        this.resource = resource;
    }

    public Res handle(Method method, Req request, Class<Res> responseType) throws IOException {
        Channel channel = null;
        try {
            channel = resource.getChannel(method);
            marshall(request, channel.getWriter());
            return unmarshall(responseType, channel.getReader());
        } finally {
            if (channel != null) {
                channel.close();
            }
        }
    }

    public Res handle(String methodName, Req request, Class<Res> responseType) throws IOException {
        return handle(Method.valueOf(methodName.toUpperCase()), request, responseType);
    }

    public Res handleDelete(Req request, Class<Res> responseType) throws IOException {
        return handle(Method.DELETE, request, responseType);
    }

    public Res handleGet(Req request, Class<Res> responseType) throws IOException {
        return handle(Method.GET, request, responseType);
    }

    public Res handleOptions(Req request, Class<Res> responseType) throws IOException {
        return handle(Method.OPTIONS, request, responseType);
    }

    public Res handlePatch(Req request, Class<Res> responseType) throws IOException {
        return handle(Method.PATCH, request, responseType);
    }

    public Res handlePost(Req request, Class<Res> responseType) throws IOException {
        return handle(Method.POST, request, responseType);
    }

    public Res handlePut(Req request, Class<Res> responseType) throws IOException {
        return handle(Method.PUT, request, responseType);
    }

    private IResource resource;

}


package com.github.chirob.jeasyrest.core.io;

import java.io.IOException;
import java.nio.charset.Charset;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;
import com.github.chirob.jeasyrest.io.Source;

public class ResourceSource extends Source {

    public ResourceSource(Resource resource, Charset charset, Method method) throws IOException {
        super(resource, charset, method);
    }

    public ResourceSource(Resource resource, String charset, Method method) throws IOException {
        super(resource, charset, method);
    }

    public ResourceSource(Resource resource, Charset charset, String method) throws IOException {
        super(resource, charset, method);
    }

    public ResourceSource(Resource resource, String charset, String method) throws IOException {
        super(resource, charset, method);
    }

    public ResourceSource(Resource resource, Charset charset) throws IOException {
        super(resource, charset, Method.GET);
    }

    public ResourceSource(Resource resource, Method method) throws IOException {
        super(resource, method);
    }

    public ResourceSource(Resource resource, String method) throws IOException {
        super(resource, method);
    }

    public ResourceSource(Resource resource) throws IOException {
        super(resource, Method.GET);
    }

    public ResourceSource(String resourcePath, Charset charset, Method method) throws IOException {
        super(Resource.getResource(resourcePath), charset, method);
    }

    public ResourceSource(String resourcePath, String charset, Method method) throws IOException {
        super(Resource.getResource(resourcePath), charset, method);
    }

    public ResourceSource(String resourcePath, String charset, String method) throws IOException {
        super(Resource.getResource(resourcePath), charset, method);
    }

    public ResourceSource(String resourcePath, Charset charset) throws IOException {
        super(Resource.getResource(resourcePath), charset, Method.GET);
    }

    public ResourceSource(String resourcePath, Method method) throws IOException {
        super(Resource.getResource(resourcePath), method);
    }

    public ResourceSource(String resourcePath, String method) throws IOException {
        super(Resource.getResource(resourcePath), method);
    }

    public ResourceSource(String resourcePath) throws IOException {
        super(Resource.getResource(resourcePath), Method.GET);
    }

    public ResourceSource(String resourcePath, Charset charset, String method) throws IOException {
        super(Resource.getResource(resourcePath), charset, method);
    }

}

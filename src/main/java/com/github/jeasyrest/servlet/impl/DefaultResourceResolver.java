package com.github.jeasyrest.servlet.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jeasyrest.concurrent.util.Pool;
import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.ioc.util.PooledInstance;
import com.github.jeasyrest.servlet.ResourceResolver;

public class DefaultResourceResolver implements ResourceResolver {

    @Override
    public PooledInstance<IResource> resolveResource(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        final String resourcePath = request.getRequestURI().replace(request.getContextPath(), "");
        String resPathPattern = resourcePath.replaceAll("\\{\\d+\\}", ".+");
        PooledInstance<IResource> resourceIntsance = RESOURCE_POOL.get(resPathPattern);
        if (resourceIntsance == null) {
            Pool<IResource> pool = new Pool<IResource>(0, 20) {
                @Override
                protected IResource newInstance(Object... initArgs) {
                    return IResourceFinder.INSTANCE.find(resourcePath);
                }
            };
            resourceIntsance = new PooledInstance<IResource>(pool) {
            };
            RESOURCE_POOL.put(resPathPattern, resourceIntsance);
        }
        return resourceIntsance;
    }

    private static final Map<String, PooledInstance<IResource>> RESOURCE_POOL = new HashMap<String, PooledInstance<IResource>>();

}
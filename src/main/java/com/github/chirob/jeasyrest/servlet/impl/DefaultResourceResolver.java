package com.github.chirob.jeasyrest.servlet.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.concurrent.util.Pool;
import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.ioc.util.PooledInstance;
import com.github.chirob.jeasyrest.servlet.ResourceResolver;

public class DefaultResourceResolver implements ResourceResolver {

    @Override
    public PooledInstance<Resource> resolveResource(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        final String resourcePath = request.getRequestURI().replace(request.getContextPath(), "");
        String resPathPattern = resourcePath.replaceAll("\\{\\d+\\}", ".+");
        PooledInstance<Resource> resourceIntsance = RESOURCE_POOL.get(resPathPattern);
        if (resourceIntsance == null) {
            Pool<Resource> pool = new Pool<Resource>(0, 20) {
                @Override
                protected Resource newInstance(Object... initArgs) {
                    return Resource.getResource(resourcePath);
                }
            };
            resourceIntsance = new PooledInstance<Resource>(pool) {
            };
            RESOURCE_POOL.put(resPathPattern, resourceIntsance);
        }
        return resourceIntsance;
    }

    private static final Map<String, PooledInstance<Resource>> RESOURCE_POOL = new HashMap<String, PooledInstance<Resource>>();

}

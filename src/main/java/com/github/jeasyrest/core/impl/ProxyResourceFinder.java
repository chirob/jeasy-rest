package com.github.jeasyrest.core.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.reflect.TypeHierarchy;
import com.github.jeasyrest.reflect.TypeHierarchy.Mode;

public class ProxyResourceFinder implements IResourceFinder {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IResource> T find(String resourcePath) {
        final T resource = resourceFinder.find(resourcePath);
        if (resource instanceof ResourceWrapper) {
            Set<Class<?>> interfaces = getInterfaces(resource);
            interfaces.addAll(getInterfaces(((ResourceWrapper) resource).original));
            return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    interfaces.toArray(new Class[0]), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            IResource r = resource;
                            Method m = null;
                            try {
                                m = r.getClass().getMethod(method.getName(), method.getParameterTypes());
                            } catch (NoSuchMethodException e) {
                                r = ((ResourceWrapper) resource).original;
                                m = r.getClass().getMethod(method.getName(), method.getParameterTypes());
                            }
                            return m.invoke(r, args);
                        }
                    });
        }
        return resource;
    }

    public ProxyResourceFinder() {
        this(new ResourceFinder());
    }

    protected ProxyResourceFinder(IResourceFinder resourceFinder) {
        this.resourceFinder = resourceFinder;
    }

    private static Set<Class<?>> getInterfaces(IResource resource) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        TypeHierarchy typeHierarchy = new TypeHierarchy(resource.getClass(), Mode.INTERFACE);
        for (Class<?> c : typeHierarchy) {
            interfaces.add(c);
        }
        return interfaces;
    }

    private IResourceFinder resourceFinder;

}

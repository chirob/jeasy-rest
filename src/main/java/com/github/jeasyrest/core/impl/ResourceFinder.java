package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.core.io.WrapperChannel;
import com.github.jeasyrest.ioc.Injections;
import com.github.jeasyrest.ioc.util.PooledInstance;
import com.github.jeasyrest.reflect.TypeHierarchy;

public class ResourceFinder implements IResourceFinder {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IResource> T find(String resourcePath) {
        final T resource = resourceMap.get(resourcePath);
        if (resource == null) {
            final PooledInstance<IResource> pooledRemoteResource = Injections.INSTANCE.pooledInstance("remoteResource");
            IResource remoteResource = pooledRemoteResource.pop(resourcePath);
            return (T) new ResourceWrapper(remoteResource) {
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
        } else if (resource instanceof ResourceWrapper) {
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
        } else {
            return resource;
        }
    }

    private Set<Class<?>> getInterfaces(IResource resource) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        TypeHierarchy typeHierarchy = new TypeHierarchy(resource.getClass());
        for (Class<?> c : typeHierarchy) {
            if (c.isInterface()) {
                interfaces.add(c);
            }
        }
        return interfaces;
    }

    private ResourceMap resourceMap = new ResourceMap();

}

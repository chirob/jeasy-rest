package com.github.jeasyrest.core.impl;

import java.io.IOException;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.io.WrapperChannel;
import com.github.jeasyrest.ioc.Injections;
import com.github.jeasyrest.ioc.util.PooledInstance;

public class RemoteResourceFinder implements IResourceFinder {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IResource> T find(String resourcePath) {
        final PooledInstance<IResource> pooledRemoteResource = Injections.INSTANCE.pooledInstance("remoteResource");
        IResource remoteResource = pooledRemoteResource.pop(resourcePath);
        return (T) new ResourceWrapper(remoteResource) {
            @Override
            public IChannel openChannel(Method method) throws IOException {
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

}

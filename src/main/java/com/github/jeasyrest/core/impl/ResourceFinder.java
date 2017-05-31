package com.github.jeasyrest.core.impl;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.ioc.Injections;

public class ResourceFinder implements IResourceFinder {

    @Override
    public <T extends IResource> T find(String resourcePath) {
        T resource = RESOURCE_MAP.get(resourcePath);
        if (resource == null) {
            resource = REMOTE_RESOURCE_FINDER.find(resourcePath);
        }
        return resource;
    }

    private static final IResourceFinder REMOTE_RESOURCE_FINDER = Injections.INSTANCE.singleton("remoteResourceFinder");
    private static final ResourceMap RESOURCE_MAP = new ResourceMap();

}

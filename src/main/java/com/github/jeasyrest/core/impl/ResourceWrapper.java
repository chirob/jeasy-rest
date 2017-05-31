package com.github.jeasyrest.core.impl;

import java.io.IOException;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.io.Channel;

public abstract class ResourceWrapper extends Resource {

    @Override
    public Channel openChannel(Method method) throws IOException {
        return original.getChannel(method);
    }

    protected ResourceWrapper(String originalPath) {
        this(IResourceFinder.INSTANCE.find(originalPath));
    }

    protected ResourceWrapper(IResource original) {
        this.original = (Resource) original;
        init(this.original.path, this.original.pathPattern, this.original.parameters);
    }

    protected Resource original;

}

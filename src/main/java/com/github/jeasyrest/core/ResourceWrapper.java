package com.github.jeasyrest.core;

import java.io.IOException;

import com.github.jeasyrest.core.io.Channel;

public abstract class ResourceWrapper extends Resource {

    @Override
    public Channel openChannel(Method method) throws IOException {
        return original.getChannel(method);
    }

    protected ResourceWrapper(String originalPath) {
        this(Resource.getResource(originalPath));
    }
    
    protected ResourceWrapper(Resource original) {
        this.original = original;
        init(original.path, original.pathPattern, original.parameters);
    }
    
    protected Resource original;
    
}

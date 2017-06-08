package com.github.jeasyrest.core.http;

import java.io.IOException;

import com.github.jeasyrest.core.impl.Resource;
import com.github.jeasyrest.core.io.Channel;

public class RemoteResource extends Resource {

    @Override
    public Channel openChannel(Method method) throws IOException {
        return new RemoteChannel(getPath(), encoding);
    }

    public RemoteResource() throws IOException {
        this("UTF-8");
    }

    public RemoteResource(String encoding) {
        this.encoding = encoding;
    }

    protected String encoding;
}

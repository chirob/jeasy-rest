package com.github.chirob.jeasyrest.core.http;

import java.io.IOException;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.io.Channel;

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

    private String encoding;
}

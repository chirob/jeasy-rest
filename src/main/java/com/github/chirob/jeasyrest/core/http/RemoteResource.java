package com.github.chirob.jeasyrest.core.http;

import java.io.IOException;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.io.Channel;

public class RemoteResource extends Resource {

    @Override
    public Channel openChannel(Method method) throws IOException {
        return new RemoteChannel(remotePath, encoding);
    }

    public RemoteResource(String remotePath) throws IOException {
        this(remotePath, "UTF-8");
    }

    public RemoteResource(String remotePath, String encoding) {
        this.remotePath = remotePath;
        this.encoding = encoding;
    }

    private String remotePath;
    private String encoding;
}

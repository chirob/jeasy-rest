package com.github.jeasyrest.core.http;

import java.io.IOException;

import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.core.xml.JAXBProcessingResource;

public class RemoteJAXBProcessingResource<Req, Res> extends JAXBProcessingResource<Req, Res> {

    @Override
    public Channel openChannel(Method method) throws IOException {
        return new RemoteChannel(getPath(), encoding);
    }

    public RemoteJAXBProcessingResource(String requestType, String responseType) throws ClassNotFoundException {
        this("UTF-8", requestType, responseType);
    }

    @SuppressWarnings("unchecked")
    public RemoteJAXBProcessingResource(String encoding, String requestType, String responseType)
            throws ClassNotFoundException {
        super((Class<? extends Req>) typeOf(requestType), (Class<? extends Res>) typeOf(responseType));
        this.encoding = encoding;
    }

    private static Class<?> typeOf(String type) throws ClassNotFoundException {
        return Class.forName(type);
    }

    private String encoding;

    @Override
    public Res process(Req request) {
        return null;
    }
}

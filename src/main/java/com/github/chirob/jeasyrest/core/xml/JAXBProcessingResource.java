package com.github.chirob.jeasyrest.core.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBException;

import com.github.chirob.jeasyrest.core.ObjectProcessingResource;
import com.github.chirob.jeasyrest.xml.util.JAXBContexts;

public abstract class JAXBProcessingResource<Req, Res> extends ObjectProcessingResource<Req, Res> {

    @Override
    protected void marshall(Res response, Writer writer) throws IOException {
        try {
            JAXBContexts.get(getResponseContextPath()).createMarshaller().marshal(response, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Req unmarshall(Reader reader) throws IOException {
        try {
            return (Req) JAXBContexts.get(getRequestContextPath()).createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    protected JAXBProcessingResource(String contextPath) {
        this.contextPath = contextPath;
    }

    protected JAXBProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        requestContextPath = requestType.getName();
        responseContextPath = responseType.getName();
    }

    protected JAXBProcessingResource(String requestContextPath, String responseContextPath) {
        this.requestContextPath = requestContextPath;
        this.responseContextPath = responseContextPath;
    }

    private String getRequestContextPath() {
        if (contextPath == null) {
            return requestContextPath;
        } else {
            return contextPath;
        }
    }

    private String getResponseContextPath() {
        if (contextPath == null) {
            return responseContextPath;
        } else {
            return contextPath;
        }
    }

    private String contextPath;
    private String requestContextPath;
    private String responseContextPath;

}

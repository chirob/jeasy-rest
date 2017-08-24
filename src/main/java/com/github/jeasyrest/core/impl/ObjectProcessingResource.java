package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IMarshaller;
import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.IUnmarshaller;

public class ObjectProcessingResource<Req, Res> extends ProcessingResource
        implements IObjectProcessingResource<Req, Res> {

    protected IObjectProcessingResource<Req, Res> setProcessDelegate(ProcessDelegate<Req, Res> delegate) {
        this.delegate = delegate;
        return this;
    }

    protected IObjectProcessingResource<Req, Res> setMarshaller(IMarshaller<Res> marshaller) {
        this.marshaller = marshaller;
        return this;
    }

    protected IObjectProcessingResource<Req, Res> setUnmarshaller(IUnmarshaller<Req> unmarshaller) {
        this.unmarshaller = new CheckEmptyUnmarshaller<Req>(unmarshaller);
        return this;
    }

    @Override
    public void process(Reader reader, Writer writer, Method method) throws IOException {
        Req request = null;
        if (unmarshaller != null && reader != null) {
            request = unmarshaller.unmarshall(reader);
        }
        Res response = process(request, method);
        if (marshaller != null && writer != null && response != null) {
            marshaller.marshall(response, writer);
        }
    }

    @Override
    public Res process(Req request, Method method) {
        switch (method) {
        case DELETE:
            return delegate.processDelete(request, this);
        case GET:
            return delegate.processGet(request, this);
        case OPTIONS:
            return delegate.processOptions(request, this);
        case PATCH:
            return delegate.processPatch(request, this);
        case POST:
            return delegate.processPost(request, this);
        case PUT:
            return delegate.processPut(request, this);
        default:
            throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    private IMarshaller<Res> marshaller;
    private IUnmarshaller<Req> unmarshaller;

    private ProcessDelegate<Req, Res> delegate = new DefaultProcessDelegate<Req, Res>();

}
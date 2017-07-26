package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IMarshaller;
import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.IUnmarshaller;

public abstract class ObjectProcessingResource<Req, Res> extends ProcessingResource
        implements IObjectProcessingResource<Req, Res> {

    protected IObjectProcessingResource<Req, Res> setMarshaller(IMarshaller<Res> marshaller) {
        this.marshaller = marshaller;
        return this;
    }

    protected IObjectProcessingResource<Req, Res> setUnmarshaller(IUnmarshaller<Req> unmarshaller) {
        this.unmarshaller = new CheckEmptyUnmarshaller<Req>(unmarshaller);
        return this;
    }

    @Override
    public final void process(Reader reader, Writer writer) throws IOException {
        Req request = null;
        if (unmarshaller != null && reader != null) {
            request = unmarshaller.unmarshall(reader);
        }
        Res response = process(request);
        if (marshaller != null && writer != null && response != null) {
            marshaller.marshall(response, writer);
        }
    }

    private IMarshaller<Res> marshaller;
    private IUnmarshaller<Req> unmarshaller;
}
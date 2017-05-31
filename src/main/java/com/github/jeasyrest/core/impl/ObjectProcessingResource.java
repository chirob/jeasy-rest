package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IObjectProcessingResource;

public abstract class ObjectProcessingResource<Req, Res> extends ProcessingResource
        implements IObjectProcessingResource<Req, Res> {

    protected abstract void marshall(Res response, Writer writer) throws IOException;

    protected abstract Req unmarshall(Reader reader) throws IOException;

    @Override
    public final void process(Reader reader, Writer writer) throws IOException {
        Req request = unmarshall(reader);
        Res response = process(request);
        marshall(response, writer);
    }

}
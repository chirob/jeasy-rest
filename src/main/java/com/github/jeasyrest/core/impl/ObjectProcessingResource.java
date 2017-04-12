package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public abstract class ObjectProcessingResource<Req, Res> extends ProcessingResource {

    public abstract Res process(Req request);

    protected abstract void marshall(Res response, Writer writer) throws IOException;

    protected abstract Req unmarshall(Reader reader) throws IOException;

    @Override
    public final void process(Reader reader, Writer writer) throws IOException {
        Req request = unmarshall(reader);
        Res response = process(request);
        marshall(response, writer);
    }

}
package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IMarshaller;
import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.IUnmarshaller;
import com.github.jeasyrest.core.error.RSException;
import com.github.jeasyrest.core.error.RSException.Codes;

public class DefaultObjectProcessDelegate<Req, Res> extends DefaultProcessDelegate
        implements ObjectProcessDelegate<Req, Res> {

    @Override
    protected void processDelete(Reader reader, Writer writer) throws IOException {
        marshall(processDelete(unmarshall(reader), resource), writer);
    }

    @Override
    protected void processGet(Reader reader, Writer writer) throws IOException {
        marshall(processGet(unmarshall(reader), resource), writer);
    }

    @Override
    protected void processOptions(Reader reader, Writer writer) throws IOException {
        marshall(processOptions(unmarshall(reader), resource), writer);
    }

    @Override
    protected void processPatch(Reader reader, Writer writer) throws IOException {
        marshall(processPatch(unmarshall(reader), resource), writer);
    }

    @Override
    protected void processPost(Reader reader, Writer writer) throws IOException {
        marshall(processPost(unmarshall(reader), resource), writer);
    }

    @Override
    protected void processPut(Reader reader, Writer writer) throws IOException {
        marshall(processPut(unmarshall(reader), resource), writer);
    }

    @Override
    public void process(Reader reader, Writer writer, Method method) throws IOException {
        marshall(resource.process(unmarshall(reader), method), writer);
    }

    @Override
    public Res process(Req request, Method method) {
        switch (method) {
        case DELETE:
            return processDelete(request, resource);
        case GET:
            return processGet(request, resource);
        case OPTIONS:
            return processOptions(request, resource);
        case PATCH:
            return processPatch(request, resource);
        case POST:
            return processPost(request, resource);
        case PUT:
            return processPut(request, resource);
        default:
            throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    protected Res processDelete(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled DELETE resource");
    }

    protected Res processGet(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled GET resource");
    }

    protected Res processOptions(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled OPTIONS resource");
    }

    protected Res processPatch(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled PATCH resource");
    }

    protected Res processPost(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled POST resource");
    }

    protected Res processPut(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled PUT resource");
    }

    protected void marshall(Res response, Writer writer) throws IOException {
        IMarshaller<Res> marshaller = resource.getMarshaller();
        if (marshaller != null && writer != null && response != null) {
            marshaller.marshall(response, writer);
        }
    }

    protected Req unmarshall(Reader reader) throws IOException {
        IUnmarshaller<Req> unmarshaller = resource.getUnmarshaller();
        if (unmarshaller != null && reader != null) {
            return unmarshaller.unmarshall(reader);
        } else {
            return null;
        }
    }

    protected DefaultObjectProcessDelegate(ObjectProcessingResource<Req, Res> resource) {
        this.resource = resource;
    }

    private ObjectProcessingResource<Req, Res> resource;

}

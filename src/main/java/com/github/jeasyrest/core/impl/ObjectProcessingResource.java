package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IMarshaller;
import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.IUnmarshaller;
import com.github.jeasyrest.core.error.RSException;
import com.github.jeasyrest.core.error.RSException.Codes;

public class ObjectProcessingResource<Req, Res> extends ProcessingResource
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
    public final void process(Reader reader, Writer writer, Method method) throws IOException {
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
            return processDelete(request);
        case GET:
            return processGet(request);
        case OPTIONS:
            return processOptions(request);
        case PATCH:
            return processPatch(request);
        case POST:
            return processPost(request);
        case PUT:
            return processPut(request);
        default:
            throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    protected Res processDelete(Req request) {
        throw new RSException(Codes.STATUS_500, "Unhandled DELETE resource");
    }

    protected Res processGet(Req request) {
        throw new RSException(Codes.STATUS_500, "Unhandled GET resource");
    }

    protected Res processOptions(Req request) {
        throw new RSException(Codes.STATUS_500, "Unhandled OPTIONS resource");
    }

    protected Res processPatch(Req request) {
        throw new RSException(Codes.STATUS_500, "Unhandled PATCH resource");
    }

    protected Res processPost(Req request) {
        throw new RSException(Codes.STATUS_500, "Unhandled POST resource");
    }

    protected Res processPut(Req request) {
        throw new RSException(Codes.STATUS_500, "Unhandled PUT resource");
    }    

    private IMarshaller<Res> marshaller;
    private IUnmarshaller<Req> unmarshaller;
}
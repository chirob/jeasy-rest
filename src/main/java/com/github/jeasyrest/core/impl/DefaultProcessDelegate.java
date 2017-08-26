package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.error.RSException;
import com.github.jeasyrest.core.error.RSException.Codes;

public class DefaultProcessDelegate implements ProcessDelegate {

    @Override
    public void process(Reader reader, Writer writer, Method method) throws IOException {
        switch (method) {
        case DELETE:
            processDelete(reader, writer);
            break;
        case GET:
            processGet(reader, writer);
            break;
        case OPTIONS:
            processOptions(reader, writer);
            break;
        case PATCH:
            processPatch(reader, writer);
            break;
        case POST:
            processPost(reader, writer);
            break;
        case PUT:
            processPut(reader, writer);
            break;
        default:
            throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    protected void processDelete(Reader reader, Writer writer) throws IOException {
        throw new RSException(Codes.STATUS_500, "Unhandled DELETE resource");
    }

    protected void processGet(Reader reader, Writer writer) throws IOException {
        throw new RSException(Codes.STATUS_500, "Unhandled GET resource");
    }

    protected void processOptions(Reader reader, Writer writer) throws IOException {
        throw new RSException(Codes.STATUS_500, "Unhandled OPTIONS resource");
    }

    protected void processPatch(Reader reader, Writer writer) throws IOException {
        throw new RSException(Codes.STATUS_500, "Unhandled PATCH resource");
    }

    protected void processPost(Reader reader, Writer writer) throws IOException {
        throw new RSException(Codes.STATUS_500, "Unhandled POST resource");
    }

    protected void processPut(Reader reader, Writer writer) throws IOException {
        throw new RSException(Codes.STATUS_500, "Unhandled PUT resource");
    }

}

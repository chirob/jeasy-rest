package com.github.jeasyrest.core.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.jeasyrest.core.impl.ObjectProcessingResource;
import com.github.jeasyrest.xml.util.JAXBContexts;

public abstract class JAXBProcessingResource<Req, Res> extends ObjectProcessingResource<Req, Res> {

    @Override
    protected void marshall(Res response, Writer writer) throws IOException {
        try {
            marshaller.marshal(response, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Req unmarshall(Reader reader) throws IOException {
        if (reader == null) {
            return null;
        } else {
            try {
                return (Req) unmarshaller.unmarshal(reader);
            } catch (JAXBException e) {
                throw new IOException(e);
            }
        }
    }

    protected JAXBProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        try {
            unmarshaller = JAXBContexts.get(requestType).createUnmarshaller();
            marshaller = JAXBContexts.get(responseType).createMarshaller();
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

}
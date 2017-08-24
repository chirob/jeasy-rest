package com.github.jeasyrest.core.process.xml;

import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.github.jeasyrest.core.IMarshaller;
import com.github.jeasyrest.xml.util.JAXBContexts;

public class JAXBMarshaller<T> implements IMarshaller<T> {

    public JAXBMarshaller(Class<? extends T> type) {
        try {
            marshaller = JAXBContexts.get(type).createMarshaller();
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void marshall(T object, Writer writer) throws IOException {
        if (object != null && writer != null) {
            try {
                marshaller.marshal(object, writer);
            } catch (JAXBException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private Marshaller marshaller;
}

package com.github.jeasyrest.core.process.xml;

import java.io.IOException;
import java.io.Reader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.github.jeasyrest.core.IUnmarshaller;
import com.github.jeasyrest.xml.util.JAXBContexts;

public class JAXBUnmarshaller<T> implements IUnmarshaller<T> {

    public JAXBUnmarshaller(Class<? extends T> type) {
        try {
            unmarshaller = JAXBContexts.get(type).createUnmarshaller();
            unmarshaller.setEventHandler(JAXBEventHandler.INSTANCE);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T unmarshall(Reader reader) throws IOException {
        try {
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Unmarshaller unmarshaller;
}

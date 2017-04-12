package com.github.jeasyrest.xml.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class JAXBContexts {

    public static final JAXBContext get(Object key) {
        JAXBContext context = JAXB_CONTEXTS.get(key);
        if (context == null) {
            try {
                if (key instanceof CharSequence) {
                    context = JAXBContext.newInstance(key.toString());
                } else if (key instanceof Class) {
                    context = JAXBContext.newInstance((Class<?>) key);
                } else {
                    context = JAXBContext.newInstance(key.getClass().getName());
                }
            } catch (JAXBException e) {
                throw new IllegalArgumentException(e);
            }
            JAXB_CONTEXTS.put(key, context);
        }
        return context;
    }

    private static final Map<Object, JAXBContext> JAXB_CONTEXTS = new HashMap<Object, JAXBContext>();

}

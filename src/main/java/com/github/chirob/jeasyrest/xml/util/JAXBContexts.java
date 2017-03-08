package com.github.chirob.jeasyrest.xml.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class JAXBContexts {

    public static final JAXBContext get(Object key) {
        JAXBContext context = JAXB_CONTEXTS.get(key);
        if (context == null) {
            String contextPath = null;
            if (key instanceof CharSequence) {
                contextPath = key.toString();
            } else if (key instanceof Class) {
                contextPath = ((Class<?>) key).getName();
            } else {
                contextPath = key.getClass().getName();
            }
            try {
                context = JAXBContext.newInstance(contextPath);
            } catch (JAXBException e) {
                throw new IllegalArgumentException(e);
            }
            JAXB_CONTEXTS.put(key, context);
        }
        return context;
    }

    private static final Map<Object, JAXBContext> JAXB_CONTEXTS = new HashMap<Object, JAXBContext>();

}

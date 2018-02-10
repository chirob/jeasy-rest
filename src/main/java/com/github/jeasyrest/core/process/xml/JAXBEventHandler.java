package com.github.jeasyrest.core.process.xml;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class JAXBEventHandler implements ValidationEventHandler {

    public static final JAXBEventHandler INSTANCE = new JAXBEventHandler();

    @Override
    public boolean handleEvent(ValidationEvent event) {
        return false;
    }

    private JAXBEventHandler() {
    }

}

package com.github.chirob.jeasyrest.test.core.xml.jaxb;

import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import com.github.chirob.jeasyrest.test.core.AuthCoreTest;
import com.github.chirob.jeasyrest.xml.util.JAXBContexts;

public class JAXBTest extends AuthCoreTest {

    protected Customer prepareObjectRequest() {
        Customer request = new Customer();
        request.setId(100);
        request.setName("mkyong");
        request.setAge(29);
        return request;
    }

    protected String prepareStringRequest() throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContexts.get(Customer.class).createMarshaller().marshal(prepareObjectRequest(), sw);
        return sw.toString();
    }
}

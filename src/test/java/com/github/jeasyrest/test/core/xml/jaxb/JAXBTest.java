package com.github.jeasyrest.test.core.xml.jaxb;

import java.io.StringReader;

import javax.xml.bind.JAXBException;

import com.github.jeasyrest.test.core.AuthCoreTest;
import com.github.jeasyrest.xml.util.JAXBContexts;

public class JAXBTest extends AuthCoreTest {

    protected Customer prepareObjectRequest() {
        try {
            return (Customer)JAXBContexts.get(Customer.class).createUnmarshaller().unmarshal(new StringReader(prepareXmlStringRequest()));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    protected String prepareXmlStringRequest() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><customer><id>100</id><name>mkyong</name><age>29</age><address_array/></customer>";
    }

    protected String prepareJsonStringRequest() throws JAXBException {
        return "{\"id\": 100, \"name\": \"mkyong\", \"age\": 29, \"address_array\": []}";
    }
    
}

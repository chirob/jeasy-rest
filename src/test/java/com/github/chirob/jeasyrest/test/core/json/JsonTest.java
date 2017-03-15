package com.github.chirob.jeasyrest.test.core.json;

import javax.xml.bind.JAXBException;

import com.github.chirob.jeasyrest.test.core.AuthCoreTest;

public class JsonTest extends AuthCoreTest {

    protected String prepareJsonStringRequest() throws JAXBException {
        return "{\"id\": 100, \"name\": \"mkyong\", \"age\": 29, \"address_array\": []}";
    }
    
}

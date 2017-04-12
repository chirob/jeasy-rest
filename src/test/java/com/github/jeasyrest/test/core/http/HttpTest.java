package com.github.jeasyrest.test.core.http;

import javax.xml.bind.JAXBException;

import com.github.jeasyrest.server.RSServer;
import com.github.jeasyrest.test.core.AuthCoreTest;

public class HttpTest extends AuthCoreTest {

    public static void main(String[] args) throws Exception {
        RSServer.main(args);
    }

    protected String prepareJsonStringRequest() throws JAXBException {
        return "{\"id\": 100, \"name\": \"mkyong\", \"age\": 29, \"address_array\": []}";
    }

}

package com.github.jeasyrest.test.core.http;

import javax.xml.bind.JAXBException;

import com.github.jeasyrest.server.RSServer;
import com.github.jeasyrest.test.core.AuthCoreTest;

public class HttpTest extends AuthCoreTest {

    static {
        new Thread() {
            @Override
            public void run() {
                try {
                    main(new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        RSServer.main(args);
    }

    protected String prepareJsonStringRequest() throws JAXBException {
        return "{\"id\": 100, \"name\": \"mkyong\", \"age\": 29, \"address_array\": []}";
    }

}

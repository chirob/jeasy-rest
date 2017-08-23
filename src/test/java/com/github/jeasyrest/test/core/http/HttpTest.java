package com.github.jeasyrest.test.core.http;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.security.AccessController;

import javax.security.auth.Subject;
import javax.xml.bind.JAXBException;

import com.github.jeasyrest.server.RSServer;
import com.github.jeasyrest.test.core.AuthCoreTest;
import com.github.jeasyrest.test.core.xml.jaxb.Customer;
import com.github.jeasyrest.xml.util.JAXBContexts;

public class HttpTest extends AuthCoreTest {

    private static final Method START_METHOD;

    static {
        try {
            START_METHOD = HttpTest.class.getMethod("start");
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        final boolean auth = Subject.getSubject(AccessController.getContext()) != null;
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpTest.this.run(START_METHOD, null, auth);
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

        super.run();
        
        stop();
    }

    public static void start() throws Exception {
        RSServer.start(new String[0]);
    }

    public static void stop() {
        RSServer.stop();
    }
    
    public static void main(String[] args) throws Exception {
        start();
    }

    protected Customer prepareObjectRequest() {
        try {
            return (Customer) JAXBContexts.get(Customer.class).createUnmarshaller()
                    .unmarshal(new StringReader(prepareXmlStringRequest()));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    protected String prepareXmlStringRequest() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><customer><id>100</id><name>mkyong</name><age>29</age><address_array></address_array></customer>";
    }

    protected String prepareJsonStringRequest() throws JAXBException {
        return "{\"id\": 100, \"name\": \"mkyong\", \"age\": 29, \"address_array\": []}";
    }

}

package com.github.chirob.jeasyrest.test.core.xml;

import java.io.IOException;

import org.junit.Test;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;
import com.github.chirob.jeasyrest.core.client.JAXBResourceHandler;
import com.github.chirob.jeasyrest.test.core.AuthCoreTest;

public class JAXBHandlerTest extends AuthCoreTest {

    @Test
    public void runWithAuthentication() {
        run(true);
    }

    @Test
    public void runWithoutAuthentication() {
        run(false);
    }

    public void test1() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(resource);

        Customer request = perpareRequest();
        Customer response = resourceHandler.handle(Method.GET, request, Customer.class);
        printResponse(response);
    }

    public void test2() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(resource);

        Customer request = perpareRequest();
        Customer response = resourceHandler.handle("get", request, Customer.class);
        printResponse(response);
    }

    public void test3() throws IOException {
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(
                "/test/services/echo");

        Customer request = perpareRequest();
        Customer response = resourceHandler.handle(Method.GET, request, Customer.class);
        printResponse(response);
    }

    public void test4() throws IOException {
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(
                "/test/services/echo");

        Customer request = perpareRequest();
        Customer response = resourceHandler.handle("get", request, Customer.class);
        printResponse(response);
    }

    private void printResponse(Customer response) {
        out.println(response);
        out.flush();
    }

    private static Customer perpareRequest() {
        Customer request = new Customer();
        request.setId(100);
        request.setName("mkyong");
        request.setAge(29);
        return request;
    }
}

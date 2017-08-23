package com.github.jeasyrest.test.core.xml.jaxb;

import java.io.IOException;

import org.junit.Test;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.client.JAXBResourceHandler;

public class JAXBHandlerTest extends JAXBTest {

    @Test
    public void runWithAuthentication() {
        run(RUN_METHOD, this, true);
    }

    @Test
    public void runWithoutAuthentication() {
        run(RUN_METHOD, this, false);
    }

    public void test1() throws IOException {
        IResource resource = IResourceFinder.INSTANCE.find("/test/services/echo");
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(resource);

        Customer request = prepareObjectRequest();
        Customer response = resourceHandler.handle(Method.GET, request, Customer.class);
        printResponse(response);
    }

    public void test2() throws IOException {
        IResource resource = IResourceFinder.INSTANCE.find("/test/services/echo");
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(resource);

        Customer request = prepareObjectRequest();
        Customer response = resourceHandler.handle("get", request, Customer.class);
        printResponse(response);
    }

    public void test3() throws IOException {
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(
                "/test/services/echo");

        Customer request = prepareObjectRequest();
        Customer response = resourceHandler.handle(Method.GET, request, Customer.class);
        printResponse(response);
    }

    public void test4() throws IOException {
        JAXBResourceHandler<Customer, Customer> resourceHandler = new JAXBResourceHandler<Customer, Customer>(
                "/test/services/echo");

        Customer request = prepareObjectRequest();
        Customer response = resourceHandler.handle("get", request, Customer.class);
        printResponse(response);
    }

    private void printResponse(Customer response) throws IOException {
        out.getWriter().write(response.toString());
    }

}
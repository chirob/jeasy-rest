package com.github.jeasyrest.test.core.http;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.http.RemoteResource;
import com.github.jeasyrest.io.Source;
import com.github.jeasyrest.test.core.xml.jaxb.Customer;

public class EchoHttpRemoteResourceTest extends HttpTest {

    @Test
    public void runWithAuthentication() {
        run(RUN_METHOD, this, true);
    }

    // @Test
    // public void runWithoutAuthentication() {
    // run(RUN_METHOD, this, false);
    // }

    public void test1() throws IOException, JAXBException {
        Source source = new Source(prepareJsonStringRequest());
        IResource resource = IResourceFinder.INSTANCE.find("http://localhost:8000/rs/test/services/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

    public void test2() throws IOException, JAXBException {
        Source source = new Source(prepareJsonStringRequest());
        IResource resource = IResourceFinder.INSTANCE.find("http://localhost:8000/rs/test/services/json/echo/2");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

    public void test3() throws IOException, JAXBException {
        Source source = new Source(prepareXmlStringRequest());
        IResource resource = IResourceFinder.INSTANCE.find("http://localhost:8000/rs/test/services/xml/jaxb/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

    public void test4() throws IOException, JAXBException {
        RemoteResource<Customer, Customer> resource = IResourceFinder.INSTANCE
                .find("http://localhost:8000/rs/test/services/xml/jaxb/echo");
        resource.getChannel(Method.GET).requestHeaders().put("Content-Type", "application/xml");
        Customer customer = resource.process(null, Method.GET);
        System.out.println(customer);
    }

    public void test5() throws IOException, JAXBException {
        RemoteResource<Customer, Customer> resource = IResourceFinder.INSTANCE
                .find("http://localhost:8000/rs/test/services/json/echo/3");
        resource.getChannel(Method.GET).requestHeaders().put("Content-Type", "application/json");
        Customer customer = resource.process(this.prepareObjectRequest(), Method.GET);
        System.out.println(customer);
    }

}
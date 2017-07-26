package com.github.jeasyrest.test.core.http;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.http.RemoteResource;
import com.github.jeasyrest.io.Source;
import com.github.jeasyrest.test.core.xml.jaxb.Customer;

public class EchoHttpRemoteResourceTest extends HttpTest {

    @Test
    public void runWithAuthentication() {
        run(true);
    }

    @Test
    public void runWithoutAuthentication() {
        run(false);
    }

    public void test1() throws IOException, JAXBException {
        Source source = new Source(prepareJsonStringRequest());
        IResource resource = IResourceFinder.INSTANCE.find("http://localhost:8000/rs/test/services/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

    public void test2() throws IOException, JAXBException {
        Source source = new Source(prepareXmlStringRequest());
        IResource resource = IResourceFinder.INSTANCE.find("http://localhost:8000/rs/test/services/xml/jaxb/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

    public void test3() throws IOException, JAXBException {
        RemoteResource<Customer, Customer> resource = IResourceFinder.INSTANCE
                .find("http://localhost:8000/rs/test/services/xml/jaxb/echo");
        resource.getRemoteChannel().setContentType("application/xml");
        Customer customer = resource.process(null);
        System.out.println(customer);
    }

    public void test4() throws IOException, JAXBException {
        RemoteResource<Customer, Customer> resource = IResourceFinder.INSTANCE
                .find("http://localhost:8000/rs/test/services/json/echo/3");
        resource.getRemoteChannel().setContentType("application/json");
        Customer customer = resource.process(this.prepareObjectRequest());
        System.out.println(customer);
    }

}
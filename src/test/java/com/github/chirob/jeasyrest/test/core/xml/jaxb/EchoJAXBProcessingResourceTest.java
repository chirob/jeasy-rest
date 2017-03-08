package com.github.chirob.jeasyrest.test.core.xml.jaxb;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.chirob.jeasyrest.core.ObjectProcessingResource;
import com.github.chirob.jeasyrest.core.ProcessingResource;
import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.io.Source;

public class EchoJAXBProcessingResourceTest extends JAXBTest {

    @Test
    public void runWithAuthentication() {
        run(true);
    }

    @Test
    public void runWithoutAuthentication() {
        run(false);
    }

    public void test1() throws IOException, JAXBException {
        ObjectProcessingResource<Customer, Customer> resource = Resource.getResource("/test/services/xml/jaxb/echo");
        Customer request = prepareObjectRequest();
        Customer response = resource.process(request);
        out.println(response);
        out.flush();
    }

    public void test2() throws IOException, JAXBException {
        Reader reader = new StringReader(prepareStringRequest());
        ProcessingResource resource = Resource.getResource("/test/services/xml/jaxb/echo");
        resource.process(reader, out);
    }

    public void test3() throws IOException, JAXBException {
        Source source = new Source(prepareStringRequest());
        ObjectProcessingResource<Customer, Customer> resource = Resource.getResource("/test/services/xml/jaxb/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }
    
}

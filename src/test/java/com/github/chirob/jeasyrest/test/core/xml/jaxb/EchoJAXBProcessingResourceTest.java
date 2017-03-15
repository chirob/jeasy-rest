package com.github.chirob.jeasyrest.test.core.xml.jaxb;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.impl.ObjectProcessingResource;
import com.github.chirob.jeasyrest.core.impl.ProcessingResource;

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
        Reader reader = new StringReader(prepareXmlStringRequest());
        ProcessingResource resource = Resource.getResource("/test/services/xml/jaxb/echo");
        resource.process(reader, out);
    }

}

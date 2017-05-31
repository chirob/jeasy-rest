package com.github.jeasyrest.test.core.xml.jaxb;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.impl.ObjectProcessingResource;
import com.github.jeasyrest.core.impl.ProcessingResource;

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
        ObjectProcessingResource<Customer, Customer> resource = IResourceFinder.INSTANCE.find("/test/services/xml/jaxb/echo");
        Customer request = prepareObjectRequest();
        Customer response = resource.process(request);
        out.getWriter().write(response.toString());
    }

    public void test2() throws IOException, JAXBException {
        Reader reader = new StringReader(prepareXmlStringRequest());
        ProcessingResource resource = IResourceFinder.INSTANCE.find("/test/services/xml/jaxb/echo");
        resource.process(reader, out.getWriter());
    }

}
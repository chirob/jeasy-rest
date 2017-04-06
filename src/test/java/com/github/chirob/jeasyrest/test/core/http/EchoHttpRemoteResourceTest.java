package com.github.chirob.jeasyrest.test.core.http;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.io.Source;

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
        Resource resource = Resource.getResource("http://localhost:8000/rs/test/services/xml/jaxb/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

}

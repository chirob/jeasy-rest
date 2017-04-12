package com.github.jeasyrest.test.core.http;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.jeasyrest.core.Resource;
import com.github.jeasyrest.io.Source;

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
        Resource resource = Resource.getResource("http://localhost:8000/rs/test/services/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        StringWriter sw = new StringWriter();
        target.writeTo(sw);
        sw.close();
        System.out.println(sw);
    }

}

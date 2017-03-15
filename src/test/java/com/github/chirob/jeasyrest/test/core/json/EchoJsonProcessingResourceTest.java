package com.github.chirob.jeasyrest.test.core.json;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.transform.json.JsonToXmlResourceTransformer;
import com.github.chirob.jeasyrest.io.Source;

public class EchoJsonProcessingResourceTest extends JsonTest {

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
        Resource resource = Resource.getResource("/test/services/xml/jaxb/echo");
        resource = new JsonToXmlResourceTransformer(resource, "customer");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

    public void test2() throws IOException, JAXBException {
        Source source = new Source(prepareJsonStringRequest());
        Resource resource = Resource.getResource("/test/services/json/echo");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

}

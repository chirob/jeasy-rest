package com.github.jeasyrest.test.core.json;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.io.Source;

public class EchoJSONProcessingResourceTest extends JsonTest {

    @Test
    public void runWithAuthentication() {
        run(RUN_METHOD, this, true);
    }

    @Test
    public void runWithoutAuthentication() {
        run(RUN_METHOD, this, false);
    }

    public void test1() throws IOException, JAXBException {
        Source source = new Source(prepareJsonStringRequest());
        IResource resource = IResourceFinder.INSTANCE.find("/test/services/json/echo/1");
        Source target = new Source(resource, "utf-8", "get");
        source.writeTo(target);
        target.writeTo(out);
    }

}
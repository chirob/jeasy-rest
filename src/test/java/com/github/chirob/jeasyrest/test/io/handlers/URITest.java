package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;
import com.github.chirob.jeasyrest.test.BaseTest;

public class URITest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }    
    
    public void test1() throws IOException, URISyntaxException {
        URI uri = getClass().getResource("/META-INF/jeasyrest/stream.handlers").toURI();
        Source source = new Source(uri, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    
    public void test2() throws IOException, URISyntaxException {
        URI uri = getClass().getResource("/META-INF/jeasyrest/stream.handlers").toURI();
        Source source = new Source(uri, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

package com.github.jeasyrest.test.io.handlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.github.jeasyrest.io.Source;
import com.github.jeasyrest.test.BaseTest;

public class URITest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }    
    
    public void test1() throws IOException, URISyntaxException {
        URI uri = getClass().getResource("/META-INF/jeasyrest/stream.handlers").toURI();
        Source source = new Source(uri, "utf8");
        source.writeTo(out);
    }

    
    public void test2() throws IOException, URISyntaxException {
        URI uri = getClass().getResource("/META-INF/jeasyrest/stream.handlers").toURI();
        Source source = new Source(uri, "utf8");
        source.writeTo(out, "utf8");
    }

}

package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public class URITest extends BaseTest {

    @Test
    public void test1() throws IOException, URISyntaxException {
        URI uri = getClass().getResource("/jeasyrest/stream.handlers").toURI();
        Source source = new Source(uri, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test2() throws IOException, URISyntaxException {
        URI uri = getClass().getResource("/jeasyrest/stream.handlers").toURI();
        Source source = new Source(uri, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

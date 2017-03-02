package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public class URLTest extends BaseTest {

    @Test
    public void test1() throws IOException {
        URL url = getClass().getResource("/jeasyrest/stream.handlers");
        Source source = new Source(url, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test2() throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("jeasyrest/stream.handlers");
        Source source = new Source(url, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

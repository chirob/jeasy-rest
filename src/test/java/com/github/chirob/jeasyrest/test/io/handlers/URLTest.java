package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.test.BaseTest;

public class URLTest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }

    public void test1() throws IOException {
        URL url = getClass().getResource("/META-INF/jeasyrest/stream.handlers");
        Source source = new Source(url, "utf8");
        source.writeTo(out);
    }

    public void test2() throws IOException {
        URL url = getClass().getResource("/META-INF/jeasyrest/stream.handlers");
        Source source = new Source(url, "utf8");
        source.writeTo(out, "utf8");
    }

}

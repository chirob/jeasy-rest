package com.github.jeasyrest.test.io.handlers;

import java.io.IOException;
import java.net.URLConnection;

import org.junit.Test;

import com.github.jeasyrest.io.Source;
import com.github.jeasyrest.test.BaseTest;

public class URLConnectionTest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }

    public void test1() throws IOException {
        URLConnection urlConnection = getClass().getResource("/META-INF/jeasyrest/stream.handlers").openConnection();
        Source source = new Source(urlConnection, "utf8");
        source.writeTo(out);
    }

    public void test2() throws IOException {
        URLConnection urlConnection = getClass().getResource("/META-INF/jeasyrest/stream.handlers").openConnection();
        Source source = new Source(urlConnection, "utf8");
        source.writeTo(out);
    }

}

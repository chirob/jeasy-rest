package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.IOException;
import java.net.URLConnection;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public class URLConnectionTest extends BaseTest {

    @Test
    public void test1() throws IOException {
        URLConnection urlConnection = getClass().getResource("/jeasyrest/stream.handlers").openConnection();
        Source source = new Source(urlConnection, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test2() throws IOException {
        URLConnection urlConnection = getClass().getResource("/jeasyrest/stream.handlers").openConnection();
        Source source = new Source(urlConnection, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

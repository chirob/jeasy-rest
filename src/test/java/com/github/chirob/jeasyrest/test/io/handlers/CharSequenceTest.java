package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.IOException;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public class CharSequenceTest extends BaseTest {

    @Test
    public void test1() throws IOException {
        String urlString = getClass().getResource("/jeasyrest/stream.handlers").toString();
        Source source = new Source(urlString, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test2() throws IOException {
        String fileString = getClass().getResource("/jeasyrest/stream.handlers").getPath();
        Source source = new Source(fileString, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test3() throws IOException {
        String textString = "\nTanto va la gatta al lardo che ci lascia lo zampino\n";
        Source source = new Source(textString);
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test4() throws IOException {
        String urlString = getClass().getResource("/jeasyrest/stream.handlers").toString();
        Source source = new Source(urlString, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

    @Test
    public void test5() throws IOException {
        String fileString = getClass().getResource("/jeasyrest/stream.handlers").getPath();
        Source source = new Source(fileString, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

    @Test
    public void test6() throws IOException {
        String textString = "\nTanto va la gatta al lardo che ci lascia lo zampino\n";
        Source source = new Source(textString);
        source.writeTo(new Source(out, "utf8"));
    }

}

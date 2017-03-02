package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public class ReaderTest extends BaseTest {

    @Test
    public void test1() throws IOException {
        String filePath = getClass().getResource("/jeasyrest/stream.handlers").getPath();
        Reader reader = new FileReader(filePath);
        Source source = new Source(reader);
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test2() throws IOException {
        String filePath = getClass().getResource("/jeasyrest/stream.handlers").getPath();
        Reader reader = new FileReader(filePath);
        Source source = new Source(reader);
        source.writeTo(new Source(out, "utf8"));
    }

}

package com.github.jeasyrest.test.io.handlers;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.github.jeasyrest.io.Source;
import com.github.jeasyrest.test.BaseTest;

public class ReaderTest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }    
    
    public void test1() throws IOException {
        String filePath = getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath();
        Reader reader = new FileReader(filePath);
        Source source = new Source(reader);
        source.writeTo(out);
    }

    
    public void test2() throws IOException {
        String filePath = getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath();
        Reader reader = new FileReader(filePath);
        Source source = new Source(reader);
        source.writeTo(out, "utf8");
    }

}

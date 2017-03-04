package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;
import com.github.chirob.jeasyrest.test.BaseTest;

public class InputStreamTest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }    
    
    public void test1() throws IOException {
        String filePath = getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath();
        InputStream inputStream = new FileInputStream(filePath);
        Source source = new Source(inputStream, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    
    public void test2() throws IOException {
        String filePath = getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath();
        InputStream inputStream = new FileInputStream(filePath);
        Source source = new Source(inputStream, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;
import com.github.chirob.jeasyrest.test.BaseTest;

public class FileTest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }    
    
    public void test1() throws IOException {
        File file = new File(getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath());
        Source source = new Source(file, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    
    public void test2() throws IOException {
        File file = new File(getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath());
        Source source = new Source(file, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.File;
import java.io.IOException;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;
import com.github.chirob.jeasyrest.test.BaseTest;

public class DataSourceTest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }
    
    public void test1() throws IOException {
        DataSource dataSource = new FileDataSource(
                new File(getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath()));
        Source source = new Source(dataSource, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    
    public void test2() throws IOException {
        DataSource dataSource = new FileDataSource(
                new File(getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath()));
        Source source = new Source(dataSource, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

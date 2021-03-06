package com.github.jeasyrest.test.io.handlers;

import java.io.File;
import java.io.IOException;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.junit.Test;

import com.github.jeasyrest.io.Source;
import com.github.jeasyrest.test.BaseTest;

public class DataSourceTest extends BaseTest {

    @Test
    public void run() {
        super.run();
    }
    
    public void test1() throws IOException {
        DataSource dataSource = new FileDataSource(
                new File(getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath()));
        Source source = new Source(dataSource, "utf8");
        source.writeTo(out);
    }

    
    public void test2() throws IOException {
        DataSource dataSource = new FileDataSource(
                new File(getClass().getResource("/META-INF/jeasyrest/stream.handlers").getPath()));
        Source source = new Source(dataSource, "utf8");
        source.writeTo(out, "utf8");
    }

}

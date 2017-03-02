package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.File;
import java.io.IOException;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.junit.Test;

import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public class DataSourceTest extends BaseTest {

    @Test
    public void test1() throws IOException {
        DataSource dataSource = new FileDataSource(
                new File(getClass().getResource("/jeasyrest/stream.handlers").getPath()));
        Source source = new Source(dataSource, "utf8");
        IOUtils.write(source.getReader(), true, out, true);
    }

    @Test
    public void test2() throws IOException {
        DataSource dataSource = new FileDataSource(
                new File(getClass().getResource("/jeasyrest/stream.handlers").getPath()));
        Source source = new Source(dataSource, "utf8");
        source.writeTo(new Source(out, "utf8"));
    }

}

package com.github.chirob.jeasyrest.test.core.io.handlers;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;
import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.core.io.ResourceSource;
import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.io.util.IOUtils;
import com.github.chirob.jeasyrest.test.core.AuthCoreTest;

public class EchoResourceTest extends AuthCoreTest {

    @Test
    public void run() {
        super.run();
    }    
    
    public void test1() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        Source target = new Source(resource, "utf8", Method.GET);
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test2() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        Source target = new Source(resource, "utf8", "get");
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test3() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        ResourceSource target = new ResourceSource(resource, "utf8", Method.GET);
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test4() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        ResourceSource target = new ResourceSource(resource, "utf8", "get");
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test5() throws IOException {
        ResourceSource target = new ResourceSource("/test/services/echo", "utf8", Method.GET);
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test6() throws IOException {
        ResourceSource target = new ResourceSource("/test/services/echo", "utf8", "get");
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test7() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        ResourceSource target = new ResourceSource(resource, Method.GET);
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test8() throws IOException {
        ResourceSource target = new ResourceSource("/test/services/echo", Method.GET);
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test9() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        ResourceSource target = new ResourceSource(resource, "get");
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test10() throws IOException {
        ResourceSource target = new ResourceSource("/test/services/echo", "get");
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test11() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        ResourceSource target = new ResourceSource(resource, Charset.forName("utf-8"));
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test12() throws IOException {
        ResourceSource target = new ResourceSource("/test/services/echo", Charset.forName("utf-8"));
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test13() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        ResourceSource target = new ResourceSource(resource);
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test14() throws IOException {
        ResourceSource target = new ResourceSource("/test/services/echo");
        new Source("Hello, Resource!").writeTo(target);
        IOUtils.write(target.getReader(), true, out, true);
    }

    public void test15() throws IOException {
        Channel channel = Resource.getResource("/test/services/echo").getChannel(Method.GET);
        new Source(channel).writeTo(out);
    }    
}

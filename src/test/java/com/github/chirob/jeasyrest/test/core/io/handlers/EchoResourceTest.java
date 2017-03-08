package com.github.chirob.jeasyrest.test.core.io.handlers;

import java.io.IOException;

import org.junit.Test;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;
import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.io.Source;
import com.github.chirob.jeasyrest.test.core.AuthCoreTest;

public class EchoResourceTest extends AuthCoreTest {

    @Test
    public void runWithAuthentication() {
        run(true);
    }

    @Test
    public void runWithoutAuthentication() {
        run(false);
    }

    public void test1() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        Source target = new Source(resource, "utf8", Method.GET);
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test2() throws IOException {
        Resource resource = Resource.getResource("/test/services/echo");
        Source target = new Source(resource, "utf8", "get");
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test3() throws IOException {
        Resource target = Resource.getResource("/test/services/echo");
        new Source("Hello, Resource!").writeTo(target, "utf8", Method.GET);
        new Source(target, "get").writeTo(out);
    }

    public void test4() throws IOException {
        Resource target = Resource.getResource("/test/services/echo");
        new Source("Hello, Resource!").writeTo(target, "utf8", "get");
        new Source(target, Method.GET).writeTo(out);
    }

    public void test5() throws IOException {
        Channel channel = Resource.getResource("/test/services/echo").getChannel(Method.GET);
        Source target = new Source(channel, "utf8");
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test6() throws IOException {
        Channel channel = Resource.getResource("/test/services/echo").getChannel("get");
        Source target = new Source(channel, "utf8");
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test7() throws IOException {
        Channel target = Resource.getResource("/test/services/echo").getChannel(Method.GET);
        new Source("Hello, Resource!").writeTo(target, "utf8");
        new Source(target).writeTo(out);
    }

    public void test8() throws IOException {
        Channel target = Resource.getResource("/test/services/echo").getChannel("get");
        new Source("Hello, Resource!").writeTo(target, "utf8");
        new Source(target).writeTo(out);
    }
}

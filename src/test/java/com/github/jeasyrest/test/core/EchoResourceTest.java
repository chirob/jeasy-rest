package com.github.jeasyrest.test.core;

import java.io.IOException;

import org.junit.Test;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.IResourceFinder;
import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.io.Source;

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
        IResource resource = IResourceFinder.INSTANCE.find("/test/services/echo");
        Source target = new Source(resource, "utf8", Method.GET);
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test2() throws IOException {
        IResource resource = IResourceFinder.INSTANCE.find("/test/services/echo");
        Source target = new Source(resource, "utf8", "get");
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test3() throws IOException {
        IResource target = IResourceFinder.INSTANCE.find("/test/services/echo");
        new Source("Hello, Resource!").writeTo(new Source(target, "utf8", Method.GET));
        new Source(target, "get").writeTo(out);
    }

    public void test4() throws IOException {
        IResource target = IResourceFinder.INSTANCE.find("/test/services/echo");
        new Source("Hello, Resource!").writeTo(new Source(target, "utf8", "get"));
        new Source(target, Method.GET).writeTo(out);
    }

    public void test5() throws IOException {
        Channel channel = IResourceFinder.INSTANCE.find("/test/services/echo").getChannel(Method.GET);
        Source target = new Source(channel, "utf8");
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test6() throws IOException {
        Channel channel = IResourceFinder.INSTANCE.find("/test/services/echo").getChannel("get");
        Source target = new Source(channel, "utf8");
        new Source("Hello, Resource!").writeTo(target);
        target.writeTo(out);
    }

    public void test7() throws IOException {
        Channel target = IResourceFinder.INSTANCE.find("/test/services/echo").getChannel(Method.GET);
        new Source("Hello, Resource!").writeTo(new Source(target, "utf8"));
        new Source(target).writeTo(out);
    }

    public void test8() throws IOException {
        Channel target = IResourceFinder.INSTANCE.find("/test/services/echo").getChannel("get");
        new Source("Hello, Resource!").writeTo(new Source(target, "utf8"));
        new Source(target).writeTo(out);
    }
}
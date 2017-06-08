package com.github.jeasyrest.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jeasyrest.servlet.RSHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class HttpServerHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        committed = false;
        this.httpExchange = httpExchange;
        HttpServletRequest request = getHttpHandler(HttpServletRequest.class);
        HttpServletResponse response = getHttpHandler(HttpServletResponse.class);
        try {
            servlet.service(request, response);
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    public String getMethod() {
        return httpExchange.getRequestMethod();
    }

    public String getContextPath() {
        return httpExchange.getHttpContext().getPath();
    }

    public String getRequestURI() {
        return httpExchange.getRequestURI().toString();
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer(getRequestURI());
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), "UTF-8"));
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(httpExchange.getResponseBody(), "UTF-8"), true);
    }

    public void reset() {
    }

    public void flushBuffer() {
        httpExchange.close();
        committed = true;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setStatus(int sc) {
        try {
            httpExchange.sendResponseHeaders(sc, 0);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getHttpHandler(Class<T> servletInterface) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { servletInterface }, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Method proxyMethod = null;
                        for (Method m : HttpServerHandler.class.getMethods()) {
                            if (m.getName().equals(method.getName())) {
                                proxyMethod = m;
                                break;
                            }
                        }
                        if (proxyMethod == null) {
                            throw new UnsupportedOperationException("Method not implemented: " + method);
                        } else {
                            return proxyMethod.invoke(HttpServerHandler.this, args);
                        }
                    }
                });
    }

    private boolean committed;
    private HttpExchange httpExchange;
    private RSHandler servlet = new RSHandler();
}
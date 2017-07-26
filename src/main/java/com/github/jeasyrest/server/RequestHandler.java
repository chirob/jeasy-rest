package com.github.jeasyrest.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class RequestHandler {

    public String getMethod() {
        return httpExchange.getRequestMethod();
    }

    public String getContextPath() {
        return httpExchange.getHttpContext().getPath();
    }

    public Enumeration<String> getHeaders(String name) {
        final String key = name;
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public String nextElement() {
                return it.next();
            }

            Iterator<String> it = httpExchange.getRequestHeaders().get(key).iterator();
        };
    }

    public Enumeration<String> getHeaderNames() {
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public String nextElement() {
                return it.next();
            }

            Iterator<String> it = httpExchange.getRequestHeaders().keySet().iterator();
        };
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

    RequestHandler(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    private HttpExchange httpExchange;

}

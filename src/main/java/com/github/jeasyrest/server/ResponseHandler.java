package com.github.jeasyrest.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;

import com.github.jeasyrest.io.util.IOUtils;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class ResponseHandler {

    public void addHeader(String name, String value) {
        httpExchange.getResponseHeaders().add(name, value);
    }

    public Collection<String> getHeaders(String name) {
        return httpExchange.getResponseHeaders().get(name);
    }

    public Collection<String> getHeaderNames() {
        return httpExchange.getResponseHeaders().keySet();
    }

    public void setHeader(String name, String value) {
        httpExchange.getResponseHeaders().set(name, value);
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(getResponseWriter(), true);
    }

    public void reset() {
    }

    public void flushBuffer() throws IOException {
        IOUtils.write(new ByteArrayInputStream(getResponseWriter().toString().getBytes("UTF-8")), false,
                httpExchange.getResponseBody(), false);
        responseWriter = null;
        httpExchange.close();
        committed = true;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setStatus(int sc) throws IOException {
        httpExchange.sendResponseHeaders(sc, getResponseWriter().toString().getBytes("UTF-8").length);
    }

    public void sendError(int sc, String errorMessage) throws IOException {
        setStatus(sc);
        IOUtils.write(new ByteArrayInputStream(errorMessage.getBytes("UTF-8")), false, httpExchange.getResponseBody(),
                false);
    }

    ResponseHandler(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    private Writer getResponseWriter() {
        if (responseWriter == null) {
            responseWriter = new StringWriter();
        }
        return responseWriter;
    }

    private Writer responseWriter;
    private boolean committed;
    private HttpExchange httpExchange;

}

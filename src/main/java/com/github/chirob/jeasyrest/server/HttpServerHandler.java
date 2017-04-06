package com.github.chirob.jeasyrest.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.servlet.RSHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class HttpServerHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        HttpServletRequest request = getHttpHandler(HttpServletRequest.class);
        HttpServletResponse response = getHttpHandler(HttpServletResponse.class);
        try {
            servlet.service(request, response);
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    public String getRequestURI() {
        return httpExchange.getRequestURI().toString();
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer(getRequestURI());
    }

    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {

            public int readLine(byte[] b, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }

            public int read(byte[] b) throws IOException {
                return rl = in.read(b);
            }

            public int read(byte[] b, int off, int len) throws IOException {
                return rl = in.read(b, off, len);
            }

            public boolean isFinished() {
                return rl == -1;
            }

            public boolean isReady() {
                return true;
            }

            public void setReadListener(ReadListener paramReadListener) {
                throw new UnsupportedOperationException();
            }

            public int read() throws IOException {
                return rl = in.read();
            }

            private int rl;
            private InputStream in = httpExchange.getRequestBody();
        };

    }

    public void reset() {
    }

    public void flushBuffer() {
    }

    public void setStatus(int sc) {
        try {
            httpExchange.sendResponseHeaders(sc, 0);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {

            public boolean isReady() {
                return true;
            }

            public void setWriteListener(WriteListener writeListener) {
                throw new UnsupportedOperationException();
            }

            public void write(byte[] b) throws IOException {
                out.write(b);
            }

            public void write(byte[] b, int off, int len) throws IOException {
                out.write(b, off, len);
            }

            public void flush() throws IOException {
                out.flush();
            }

            public void close() throws IOException {
                out.close();
            }

            public void write(int b) throws IOException {
                out.write(b);
            }

            private OutputStream out = httpExchange.getResponseBody();
        };
    }

    @SuppressWarnings("unchecked")
    private <T> T getHttpHandler(Class<T> servletInterface) {
        return (T) Proxy.newProxyInstance(HttpServerHandler.class.getClassLoader(), new Class[] { servletInterface },
                new InvocationHandler() {
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
                            throw new UnsupportedOperationException();
                        } else {
                            return proxyMethod.invoke(HttpServerHandler.this, args);
                        }
                    }
                });
    }


    private HttpExchange httpExchange;
    private RSHandler servlet = new RSHandler();
}

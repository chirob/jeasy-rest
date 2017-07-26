package com.github.jeasyrest.server;

import java.io.IOException;
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
        requestHandler = new RequestHandler(httpExchange);
        responseHandler = new ResponseHandler(httpExchange);
        HttpServletRequest request = getHttpHandler(HttpServletRequest.class);
        HttpServletResponse response = getHttpHandler(HttpServletResponse.class);
        try {
            servlet.service(request, response);
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getHttpHandler(final Class<T> servletInterface) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { servletInterface }, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Method proxyMethod = null;
                        Object handler = HttpServletRequest.class.equals(servletInterface) ? requestHandler
                                : responseHandler;
                        for (Method m : handler.getClass().getMethods()) {
                            if (m.getName().equals(method.getName())) {
                                proxyMethod = m;
                                break;
                            }
                        }
                        if (proxyMethod == null) {
                            throw new UnsupportedOperationException("Method not implemented: " + method);
                        } else {
                            return proxyMethod.invoke(handler, args);
                        }
                    }
                });
    }

    private RequestHandler requestHandler;
    private ResponseHandler responseHandler;
    private RSHandler servlet = new RSHandler();
}
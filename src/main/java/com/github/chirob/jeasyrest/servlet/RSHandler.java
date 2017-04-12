package com.github.chirob.jeasyrest.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.ioc.Injections;
import com.github.chirob.jeasyrest.ioc.util.PooledInstance;

public class RSHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        PooledInstance<Resource> resourceInstance = null;

        response.reset();

        try {
            ResourceResolver resourceResolver = Injections.INSTANCE.singleton("servletResourceResolver");
            resourceInstance = resourceResolver.resolveResource(request, response);

            ResourceHandler resourceHandler = Injections.INSTANCE.singleton("servletResourceHandler");
            resourceHandler.handleResource(request, response, resourceInstance.pop());

            response.setStatus(200);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            ExceptionHandler exceptionHandler = Injections.INSTANCE.singleton("servletExceptionHandler");
            exceptionHandler.handleException(request, response, throwable);
        } finally {
            if (resourceInstance != null) {
                resourceInstance.release();
            }
            if (!response.isCommitted()) {
                response.flushBuffer();
            }
        }
    }

}

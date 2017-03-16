package com.github.chirob.jeasyrest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.ioc.Injections;
import com.github.chirob.jeasyrest.ioc.util.PooledInstance;

public class RSHandlerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        PooledInstance<ServletResourceResolver> resourceResolverInstance = null;
        PooledInstance<ServletResourceHandler> resourceHandlerInstance = null;
        PooledInstance<ServletExceptionHandler> exceptionHandlerInstance = null;
        
        response.reset();
        
        try {
            resourceResolverInstance = Injections.INSTANCE.pooledInstance("servletResourceResolver");
            Resource resource = resourceResolverInstance.pop().resolveResource(request, response);
            
            resourceHandlerInstance = Injections.INSTANCE.pooledInstance("servletResourceHandler");
            resourceHandlerInstance.pop().handleResource(request, response, resource);
            
            response.setStatus(200);
        } catch (Throwable throwable) {
            exceptionHandlerInstance = Injections.INSTANCE.pooledInstance("servletExceptionHandler");            
            exceptionHandlerInstance.pop().handleException(request, response, throwable);
        } finally {
            resourceResolverInstance.release();
            resourceHandlerInstance.release();
            exceptionHandlerInstance.release();
            
            if (!response.isCommitted()) {
                response.flushBuffer();
            }
        }
    }

}

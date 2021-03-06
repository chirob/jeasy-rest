package com.github.jeasyrest.servlet.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.impl.ProcessingResource;
import com.github.jeasyrest.io.util.IOUtils;
import com.github.jeasyrest.servlet.ResourceHandler;

public class DefaultResourceHandler implements ResourceHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultResourceHandler.class);

    @Override
    public void handleResource(HttpServletRequest request, HttpServletResponse response, IResource resource)
            throws IOException, ServletException {
        Reader requestReader = null;
        Writer responseWriter = null;
        Reader resourceReader = null;
        Writer resourceWriter = null;
        long startTime = System.currentTimeMillis();
        try {
            Method method = Method.valueOf(request.getMethod());

            requestReader = request.getReader();
            responseWriter = response.getWriter();
            if (resource instanceof ProcessingResource) {
                ((ProcessingResource) resource).process(requestReader, responseWriter, method);
            } else {
                IChannel channel = resource.getChannel(method);

                resourceWriter = channel.getWriter();
                if (resourceWriter != null) {
                    IOUtils.write(requestReader, true, resourceWriter, true);
                }

                resourceReader = channel.getReader();
                if (resourceReader != null) {
                    IOUtils.write(resourceReader, true, responseWriter, true);
                }

                channel.close();
            }
        } finally {
            IOUtils.close(requestReader, responseWriter, resourceReader, resourceWriter);
            long endTime = System.currentTimeMillis();
            logger.info("Handling resource " + resource.getPath() + " elapsed time (millis): {}",
                    (endTime - startTime));
        }
    }

}

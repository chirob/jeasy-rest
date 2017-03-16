package com.github.chirob.jeasyrest.servlet.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;
import com.github.chirob.jeasyrest.core.impl.ProcessingResource;
import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.core.io.UnavailableStreamException;
import com.github.chirob.jeasyrest.io.util.IOUtils;
import com.github.chirob.jeasyrest.servlet.ResourceHandler;

public class DefaultResourceHandler implements ResourceHandler {

    @Override
    public void handleResource(HttpServletRequest request, HttpServletResponse response, Resource resource)
            throws IOException, ServletException {
        Reader requestReader = null;
        Writer responseWriter = null;
        Reader resourceReader = null;
        Writer resourceWriter = null;

        try {
            Method method = Method.valueOf(request.getMethod());

            requestReader = request.getReader();
            responseWriter = response.getWriter();
            if (resource instanceof ProcessingResource) {
                ((ProcessingResource) resource).process(requestReader, responseWriter);
            } else {
                Channel channel = resource.getChannel(method);

                try {
                    resourceWriter = channel.getWriter();
                } catch (UnavailableStreamException e) {
                }
                if (resourceWriter != null) {
                    IOUtils.write(requestReader, true, resourceWriter, true);
                }

                try {
                    resourceReader = channel.getReader();
                } catch (UnavailableStreamException e) {
                }
                if (resourceReader != null) {
                    IOUtils.write(resourceReader, true, responseWriter, true);
                }
            }
        } finally {
            IOUtils.close(requestReader, responseWriter, resourceReader, resourceWriter);
        }
    }

}

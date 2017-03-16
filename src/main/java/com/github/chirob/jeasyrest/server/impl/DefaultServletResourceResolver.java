package com.github.chirob.jeasyrest.server.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.server.ServletResourceResolver;

public class DefaultServletResourceResolver implements ServletResourceResolver {

    @Override
    public Resource resolveResource(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        return null;
    }

}

package com.github.chirob.jeasyrest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.core.Resource;

public interface ServletResourceResolver {

    Resource resolveResource(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;

}

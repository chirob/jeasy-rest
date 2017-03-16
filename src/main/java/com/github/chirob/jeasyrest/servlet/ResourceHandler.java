package com.github.chirob.jeasyrest.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.core.Resource;

public interface ResourceHandler {

    void handleResource(HttpServletRequest request, HttpServletResponse response, Resource resource)
            throws IOException, ServletException;

}

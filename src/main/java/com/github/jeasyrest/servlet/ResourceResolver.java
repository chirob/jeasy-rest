package com.github.jeasyrest.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jeasyrest.core.Resource;
import com.github.jeasyrest.ioc.util.PooledInstance;

public interface ResourceResolver {

    PooledInstance<Resource> resolveResource(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;

}

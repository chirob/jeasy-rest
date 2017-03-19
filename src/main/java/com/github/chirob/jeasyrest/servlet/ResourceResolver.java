package com.github.chirob.jeasyrest.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.ioc.util.PooledInstance;

public interface ResourceResolver {

    PooledInstance<Resource> resolveResource(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;

}

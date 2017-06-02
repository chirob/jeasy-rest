package com.github.jeasyrest.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jeasyrest.core.impl.RunningContext;

class ServletRunningContext extends RunningContext {

    private static final ServletRunningContext INSTANCE = new ServletRunningContext();

    static void init(HttpServletRequest request, HttpServletResponse response) {
        INSTANCE.put(ServletRequest.class, request);
        INSTANCE.put(ServletResponse.class, response);
    }

    private ServletRunningContext() {
    }

}

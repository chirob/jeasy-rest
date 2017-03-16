package com.github.chirob.jeasyrest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletExceptionHandler {

    void handleException(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
            throws IOException, ServletException;

}

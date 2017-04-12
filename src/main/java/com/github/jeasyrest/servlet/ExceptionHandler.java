package com.github.jeasyrest.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler {

    void handleException(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
            throws IOException, ServletException;

}

package com.github.chirob.jeasyrest.server.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chirob.jeasyrest.server.ServletExceptionHandler;

public class DefaultServletExceptionHandler implements ServletExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServletExceptionHandler.class);

    @Override
    public void handleException(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
            throws IOException, ServletException {
        HttpError error = new HttpError(throwable);
        response.sendError(error.status, error.message);
    }

    private static final class HttpError {
        private HttpError(Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);

            ExceptionType exceptionType = ExceptionType.valueOf(throwable.getClass().getSimpleName());
            switch (exceptionType) {
            case ResourceNotFoundException:
                status = 404;
                break;
            default:
                status = 500;
            }
            message = errorMessage(throwable);
        }

        private int status;
        private String message;

        private static String errorMessage(Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            pw.close();
            return sw.toString();
        }
    }

    private static enum ExceptionType {
        ResourceNotFoundException
    };

}

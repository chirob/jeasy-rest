package com.github.jeasyrest.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jeasyrest.io.Source;

public class BaseTest {

    public BaseTest() {
        try {
            out = new Source(new PrintWriter(new StringWriter()) {
                @Override
                public void close() {
                }

                @Override
                public void flush() {
                    logger.info(out.toString());
                    StringBuffer sb = ((StringWriter) out).getBuffer();
                    sb.delete(0, sb.length());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void run() {
        Class<? extends BaseTest> testClass = BaseTest.this.getClass();
        for (Method method : testClass.getMethods()) {
            if (!method.getName().startsWith("run") && method.getDeclaringClass().equals(testClass)) {
                long startTime = System.currentTimeMillis();
                logger.info("Start time (millis): {}", startTime);
                try {
                    method.invoke(BaseTest.this);
                } catch (InvocationTargetException e) {
                    e.getTargetException().printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    logger.info("End time (millis): {}", endTime);
                    logger.info("Method " + method.getName() + " elapsed time (millis): {}", (endTime - startTime));
                }
            }
        }
    }

    protected Source out;

    protected Logger logger = LoggerFactory.getLogger(getClass());
}
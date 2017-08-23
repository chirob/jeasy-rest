package com.github.jeasyrest.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

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
        Method[] methods = testClass.getMethods();
        Arrays.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        for (Method method : methods) {
            if (!method.getName().startsWith("run") && method.getDeclaringClass().equals(testClass)) {
                long startTime = System.currentTimeMillis();
                logger.info("\n\n********************** Method " + method.getName()
                        + " - Start time (millis): {} **********************", startTime);
                try {
                    method.invoke(BaseTest.this);
                } catch (InvocationTargetException e) {
                    logger.error(e.getTargetException().getMessage(), e.getTargetException());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    long endTime = System.currentTimeMillis();
                    logger.info("Method " + method.getName() + " - End time (millis): {}", endTime);
                    logger.info("\n********************** Method " + method.getName()
                            + " - Elapsed time (millis): {} **********************", (endTime - startTime));
                }
            }
        }
    }

    protected Source out;

    protected Logger logger = LoggerFactory.getLogger(getClass());
}
package com.github.chirob.jeasyrest.test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

    protected void run() {
        Class<? extends BaseTest> testClass = BaseTest.this.getClass();
        for (Method method : testClass.getMethods()) {
            if (!"run".equals(method.getName()) && method.getDeclaringClass().equals(testClass)) {
                try {
                    method.invoke(BaseTest.this);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Writer out = new PrintWriter(new StringWriter(), true) {
        @Override
        public void flush() {
            super.flush();
            logger.info(out.toString());
            StringBuffer sb = ((StringWriter) out).getBuffer();
            sb.delete(0, sb.length());
        }

        @Override
        public void close() {
            flush();
        }
    };

    protected Logger logger = LoggerFactory.getLogger(getClass());
}

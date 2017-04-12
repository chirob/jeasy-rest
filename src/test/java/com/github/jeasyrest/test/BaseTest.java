package com.github.jeasyrest.test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

    protected void run() {
        Class<? extends BaseTest> testClass = BaseTest.this.getClass();
        for (Method method : testClass.getMethods()) {
            if (!method.getName().startsWith("run") && method.getDeclaringClass().equals(testClass)) {
                try {
                    method.invoke(BaseTest.this);
                } catch (InvocationTargetException e) {
                    e.getTargetException().printStackTrace(out);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace(out);
                    out.flush();
                }
            }
        }
    }

    protected PrintWriter out = new PrintWriter(new StringWriter()) {
        @Override
        public void close() {
        }
        
        @Override
        public void flush() {
            logger.info(out.toString());
            StringBuffer sb = ((StringWriter) out).getBuffer();
            sb.delete(0, sb.length());
        }
    };

    protected Logger logger = LoggerFactory.getLogger(getClass());
}

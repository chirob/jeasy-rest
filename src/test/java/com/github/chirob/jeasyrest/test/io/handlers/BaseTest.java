package com.github.chirob.jeasyrest.test.io.handlers;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class BaseTest {

    protected Writer out = new FilterWriter(new StringWriter()) {
        @Override
        public void close() throws IOException {
            super.close();
            System.out.println(lock.toString());
        }
    };

}

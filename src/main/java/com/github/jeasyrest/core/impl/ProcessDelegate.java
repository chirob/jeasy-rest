package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IResource.Method;

public interface ProcessDelegate {

    void process(Reader reader, Writer writer, Method method) throws IOException;
    
}

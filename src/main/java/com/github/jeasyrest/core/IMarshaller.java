package com.github.jeasyrest.core;

import java.io.IOException;
import java.io.Writer;

public interface IMarshaller<T> {

    void marshall(T object, Writer writer) throws IOException;
    
}

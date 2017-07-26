package com.github.jeasyrest.core;

import java.io.IOException;
import java.io.Reader;

public interface IUnmarshaller<T> {

    T unmarshall(Reader reader) throws IOException;    
    
}

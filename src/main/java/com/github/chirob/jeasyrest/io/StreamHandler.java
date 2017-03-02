package com.github.chirob.jeasyrest.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface StreamHandler {

    void init(Object sourceObject, Object... sourceParams) throws IOException;
    
    Reader getReader() throws IOException;
    
    Writer getWriter() throws IOException;
    
}

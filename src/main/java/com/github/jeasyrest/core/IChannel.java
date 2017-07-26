package com.github.jeasyrest.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface IChannel {
            
    void close() throws IOException;
    
    Reader getReader() throws IOException;

    Writer getWriter() throws IOException;
    
    boolean isClosed() throws IOException;

    IHeaders requestHeaders() throws IOException;
    
    IHeaders responseHeaders() throws IOException;
    
}

package com.github.jeasyrest.core.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface Channel {
        
    void close() throws IOException;

    Reader getReader() throws IOException;

    Writer getWriter() throws IOException;
    
    boolean isClosed() throws IOException;
    
}

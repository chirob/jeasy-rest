package com.github.chirob.jeasyrest.core.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface Channel {
        
    void close();

    Reader getReader() throws IOException;

    Writer getWriter() throws IOException;
    
    boolean isClosed();
    
}

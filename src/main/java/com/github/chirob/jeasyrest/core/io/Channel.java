package com.github.chirob.jeasyrest.core.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface Channel {

    Reader getReader() throws IOException;

    Writer getWriter() throws IOException;
    
}

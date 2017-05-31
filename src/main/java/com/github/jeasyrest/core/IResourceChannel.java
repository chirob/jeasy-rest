package com.github.jeasyrest.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface IResourceChannel {

    Reader getReader() throws IOException;

    Writer getWriter() throws IOException;

}

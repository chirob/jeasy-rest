package com.github.jeasyrest.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface IProcessingResource extends IResource {

    void process(Reader reader, Writer writer, Method method) throws IOException;

}

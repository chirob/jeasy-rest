package com.github.chirob.jeasyrest.test.core;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.io.Channel;

public class EchoResource extends Resource {

    @Override
    public Channel openChannel(Method method) throws IOException {
        return new Channel() {
            @Override
            public Reader getReader() throws IOException {
                return new StringReader(writer.toString());
            }

            @Override
            public Writer getWriter() throws IOException {
                return writer;
            }

            private StringWriter writer = new StringWriter();
        };
    }

}

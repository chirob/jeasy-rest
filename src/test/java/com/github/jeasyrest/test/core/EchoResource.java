package com.github.jeasyrest.test.core;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import com.github.jeasyrest.core.impl.Resource;
import com.github.jeasyrest.core.io.Channel;

public class EchoResource extends Resource {

    @Override
    public Channel openChannel(Method method) throws IOException {
        return new Channel() {
            @Override
            public Reader getReader() throws IOException {
                return new StringReader(writer.toString()) {
                    @Override
                    public int read() throws IOException {
                        return checkEOF(super.read());
                    }

                    @Override
                    public int read(char[] cbuf, int off, int len) throws IOException {
                        return checkEOF(super.read(cbuf, off, len));
                    }

                    @Override
                    public int read(char[] cbuf) throws IOException {
                        return checkEOF(super.read(cbuf));
                    }

                    private int checkEOF(int rl) {
                        if (rl == -1) {
                            writer = null;
                        }
                        return rl;
                    }
                };
            }

            @Override
            public Writer getWriter() throws IOException {
                return writer = new StringWriter();
            }

            @Override
            public void close() {
                writer = null;
            }

            @Override
            public boolean isClosed() {
                return writer == null;
            }

            private Writer writer;

        };
    }

}
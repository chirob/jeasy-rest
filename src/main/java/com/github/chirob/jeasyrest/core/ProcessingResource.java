package com.github.chirob.jeasyrest.core;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chirob.jeasyrest.concurrent.util.ThreadExecutor;
import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public abstract class ProcessingResource extends Resource {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingResource.class);

    protected abstract void process(Reader reader, Writer writer) throws IOException;

    @Override
    public final Channel openChannel(Method method) throws IOException {
        final PipeChannel channel = new PipeChannel();

        ThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    process(channel.preaderIn, channel.pwriterOut);
                } catch (Throwable t) {
                    logger.error("Resource processing error", t);
                } finally {
                    IOUtils.close(channel.preaderIn, channel.pwriterOut);
                }
            }
        });

        return channel;
    }

    private static final class PipeChannel implements Channel {
        @Override
        public void close() {
            IOUtils.close(preaderOut, pwriterIn);
            preaderOut = null;
            pwriterIn = null;
        }

        @Override
        public Reader getReader() throws IOException {
            PipeChannel.this.close();
            return preaderOut = new PipedReader() {
                @Override
                public synchronized int read() throws IOException {
                    return checkEOF(super.read());
                }

                @Override
                public synchronized int read(char[] cbuf, int off, int len) throws IOException {
                    return checkEOF(super.read(cbuf, off, len));
                }

                @Override
                public int read(char[] cbuf) throws IOException {
                    return checkEOF(super.read(cbuf));
                }

                private int checkEOF(int rv) throws IOException {
                    if (rv == -1) {
                        close();
                    }
                    return rv;
                }

                @Override
                public void close() throws IOException {
                    preaderOut = null;
                    super.close();
                }
            };
        }

        @Override
        public Writer getWriter() throws IOException {
            PipeChannel.this.close();
            return pwriterIn = new PipedWriter(preaderIn) {
                @Override
                public void close() throws IOException {
                    pwriterIn = null;
                    super.close();
                }
            };
        }

        @Override
        public boolean isClosed() {
            return preaderOut == null && pwriterIn == null;
        }

        private PipedReader preaderIn;
        private PipedWriter pwriterIn;
        private PipedReader preaderOut;
        private PipedWriter pwriterOut;
    }

}

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

    public abstract void process(Reader reader, Writer writer) throws IOException;

    @Override
    public final Channel openChannel(Method method) throws IOException {
        final PipeChannel channel = new PipeChannel();
        final Reader reader = channel.getPipedReader();
        final Writer writer = channel.getPipedWriter();
        ThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!channel.isClosed()) {
                        process(reader, writer);
                    }
                } catch (Throwable t) {
                    logger.error("Resource processing error", t);
                    IOUtils.close(channel.preaderIn, channel.pwriterOut);
                } 
            }
        });

        return channel;
    }

    private static final class PipeChannel implements Channel {
        @Override
        public void close() {
            IOUtils.close(pwriterIn, preaderOut);
            pwriterIn = null;            
            preaderOut = null;
        }

        @Override
        public Reader getReader() throws IOException {
            PipeChannel.this.close();
            return preaderOut = new PipedReader(pwriterOut) {
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

        private Reader getPipedReader() throws IOException {
            return preaderIn = new PipedReader();
        }

        private Writer getPipedWriter() throws IOException {
            return pwriterOut = new PipedWriter();
        }
        
        private PipeChannel() {
            preaderIn = new PipedReader();
            pwriterOut = new PipedWriter();
        }

        private PipedReader preaderIn;
        private PipedWriter pwriterIn;
        private PipedReader preaderOut;
        private PipedWriter pwriterOut;
    }

}

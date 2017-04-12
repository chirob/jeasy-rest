package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.concurrent.util.ThreadExecutor;
import com.github.jeasyrest.core.Resource;
import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.io.util.IOUtils;

public abstract class ProcessingResource extends Resource {

    public abstract void process(Reader reader, Writer writer) throws IOException;

    @Override
    public Channel openChannel(Method method) throws IOException {
        return new PipeChannel(this);
    }

    private static final class PipeChannel implements Channel {
        @Override
        public void close() {
            closeStreams();
            resource = null;
        }

        @Override
        public Reader getReader() throws IOException {
            if (preaderOut == null) {
                pwriterOut = new PipedWriter();
                preaderOut = new PipedReader(pwriterOut);

                processResource();
            }
            return preaderOut;
        }

        @Override
        public Writer getWriter() throws IOException {
            preaderIn = new PipedReader();
            pwriterIn = new PipedWriter(preaderIn);
            pwriterOut = new PipedWriter();
            preaderOut = new PipedReader(pwriterOut);

            processResource();

            return pwriterIn;
        }

        @Override
        public boolean isClosed() {
            return resource == null;
        }

        private PipeChannel(ProcessingResource resource) {
            this.resource = resource;
        }

        private void closeStreams() {
            IOUtils.close(pwriterIn, pwriterOut);
        }

        private void processResource() {
            ThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        resource.process(preaderIn, pwriterOut);
                    } catch (Throwable t) {
                        throw new RuntimeException("Resource processing error", t);
                    } finally {
                        closeStreams();
                    }
                }
            });
        }

        private PipedReader preaderIn;
        private PipedWriter pwriterIn;
        private PipedReader preaderOut;
        private PipedWriter pwriterOut;

        private ProcessingResource resource;
    }

}

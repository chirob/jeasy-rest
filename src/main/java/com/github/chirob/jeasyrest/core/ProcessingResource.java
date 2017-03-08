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
            return preaderOut;
        }

        @Override
        public Writer getWriter() throws IOException {
            pwriterOut = new PipedWriter();
            preaderOut = new PipedReader(pwriterOut);
            preaderIn = new PipedReader();
            pwriterIn = new PipedWriter(preaderIn);

            ThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        resource.process(preaderIn, pwriterOut);
                    } catch (Throwable t) {
                        logger.error("Resource processing error", t);
                    } finally {
                        closeStreams();
                    }
                }
            });

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

        private PipedReader preaderIn;
        private PipedWriter pwriterIn;
        private PipedReader preaderOut;
        private PipedWriter pwriterOut;

        private ProcessingResource resource;
    }

}

package com.github.chirob.jeasyrest.core.transform;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;

import com.github.chirob.jeasyrest.concurrent.util.ThreadExecutor;
import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.ResourceWrapper;
import com.github.chirob.jeasyrest.core.io.Channel;
import com.github.chirob.jeasyrest.io.util.IOUtils;

public class ResourceTranformer extends ResourceWrapper {

    public ResourceTranformer(String originalPath) {
        super(originalPath);
    }

    public ResourceTranformer(Resource original) {
        super(original);
    }

    @Override
    public final Channel openChannel(Method method) throws IOException {
        return new PipeChannel(this, super.openChannel(method));
    }

    protected void transformIn(Reader inputReader, Writer inputWriter) throws IOException {
        IOUtils.write(inputReader, true, inputWriter, true);
    }

    protected void transformOut(Reader outputReader, Writer outputWriter) throws IOException {
        IOUtils.write(outputReader, true, outputWriter, true);
    }

    private static final class PipeChannel implements Channel {
        @Override
        public void close() {
            closeStreams();
            transformer = null;
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
                        transformer.transformIn(preaderIn, channel.getWriter());
                        transformer.transformOut(channel.getReader(), pwriterOut);
                    } catch (Throwable t) {
                        throw new RuntimeException("Resource processing error", t);
                    } finally {
                        closeStreams();
                    }
                }
            });

            return pwriterIn;
        }

        @Override
        public boolean isClosed() {
            return transformer == null;
        }

        private PipeChannel(ResourceTranformer transformer, Channel channel) {
            this.transformer = transformer;
            this.channel = channel;
        }

        private void closeStreams() {
            IOUtils.close(pwriterIn, pwriterOut);
        }

        private PipedReader preaderIn;
        private PipedWriter pwriterIn;
        private PipedReader preaderOut;
        private PipedWriter pwriterOut;

        private ResourceTranformer transformer;
        private Channel channel;
    }

}
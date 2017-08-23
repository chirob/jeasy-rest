package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.concurrent.util.ThreadExecutor;
import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IProcessingResource;
import com.github.jeasyrest.core.error.RSException;
import com.github.jeasyrest.core.error.RSException.Codes;
import com.github.jeasyrest.io.util.IOUtils;

public class ProcessingResource extends Resource implements IProcessingResource {

    @Override
    public IChannel openChannel(Method method) throws IOException {
        return new PipeChannel(this, method);
    }

    @Override
    public void process(Reader reader, Writer writer, Method method) throws IOException {
        switch (method) {
        case DELETE:
            processDelete(reader, writer);
        case GET:
            processGet(reader, writer);
        case OPTIONS:
            processOptions(reader, writer);
        case PATCH:
            processPatch(reader, writer);
        case POST:
            processPost(reader, writer);
        case PUT:
            processPut(reader, writer);
        default:
            throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    protected void processDelete(Reader reader, Writer writer) {
        throw new RSException(Codes.STATUS_500, "Unhandled DELETE resource");
    }

    protected void processGet(Reader reader, Writer writer) {
        throw new RSException(Codes.STATUS_500, "Unhandled GET resource");
    }

    protected void processOptions(Reader reader, Writer writer) {
        throw new RSException(Codes.STATUS_500, "Unhandled OPTIONS resource");
    }

    protected void processPatch(Reader reader, Writer writer) {
        throw new RSException(Codes.STATUS_500, "Unhandled PATCH resource");
    }

    protected void processPost(Reader reader, Writer writer) {
        throw new RSException(Codes.STATUS_500, "Unhandled POST resource");
    }

    protected void processPut(Reader reader, Writer writer) {
        throw new RSException(Codes.STATUS_500, "Unhandled PUT resource");
    }

    private static final class PipeChannel extends Channel {
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

        private PipeChannel(ProcessingResource resource, Method method) {
            this.resource = resource;
            this.method = method;
        }

        private void closeStreams() {
            IOUtils.close(pwriterIn, pwriterOut);
        }

        private void processResource() {
            ThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        resource.process(preaderIn, pwriterOut, method);
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
        private Method method;
    }

}
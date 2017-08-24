package com.github.jeasyrest.core.process.json;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;

import com.github.jeasyrest.concurrent.util.ThreadExecutor;
import com.github.jeasyrest.core.process.json.util.Capitalizer;
import com.github.jeasyrest.core.process.xml.JAXBUnmarshaller;
import com.github.jeasyrest.io.util.IOUtils;

public class XmlToJsonUnmarshaller<T> extends JAXBUnmarshaller<T> {

    public XmlToJsonUnmarshaller(Class<? extends T> type) {
        this(type, null);
    }

    public XmlToJsonUnmarshaller(Class<? extends T> type, String rootTag) {
        super(type);
        String root = rootTag == null ? Capitalizer.firstCharToLowerCase(type.getSimpleName()) : rootTag;
        transformer = new JsonToXmlTransformer(root);
    }

    @Override
    public T unmarshall(Reader reader) throws IOException {
        final Reader freader = reader;
        final PipedWriter pwriter = new PipedWriter();
        final PipedReader preader = new PipedReader(pwriter);
        ThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    transformer.fromXmlToJson(freader, pwriter);
                } catch (Throwable t) {
                    throw new RuntimeException("Unmarshalling error", t);
                } finally {
                    IOUtils.close(pwriter);
                }
            }
        });
        try {
            return super.unmarshall(preader);
        } finally {
            IOUtils.close(preader);
        }
    }

    private JsonToXmlTransformer transformer;
}

package com.github.jeasyrest.core.json;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Writer;

import com.github.jeasyrest.concurrent.util.ThreadExecutor;
import com.github.jeasyrest.core.xml.JAXBMarshaller;
import com.github.jeasyrest.io.util.IOUtils;

public class JsonToXmlMarshaller<T> extends JAXBMarshaller<T> {

    public JsonToXmlMarshaller(Class<? extends T> type) {
        this(type, null);
    }

    public JsonToXmlMarshaller(Class<? extends T> type, String rootTag) {
        super(type);
        String root = rootTag == null ? type.getSimpleName().toLowerCase() : rootTag;
        transformer = new JsonToXmlTransformer(root);
    }

    @Override
    public void marshall(T object, Writer writer) throws IOException {
        final T fobject = object;
        final PipedWriter pwriter = new PipedWriter();
        ThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JsonToXmlMarshaller.super.marshall(fobject, pwriter);
                } catch (Throwable t) {
                    throw new RuntimeException("Marshalling error", t);
                } finally {
                    IOUtils.close(pwriter);
                }
            }
        });
        PipedReader preader = new PipedReader(pwriter);
        try {
            transformer.fromJsonToXml(preader, writer);
        } finally {
            IOUtils.close(preader);
        }
    }

    private JsonToXmlTransformer transformer;
}

package com.github.jeasyrest.core.process.json;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Writer;

import com.github.jeasyrest.concurrent.util.ThreadExecutor;
import com.github.jeasyrest.core.process.json.util.Capitalizer;
import com.github.jeasyrest.core.process.xml.JAXBMarshaller;
import com.github.jeasyrest.io.util.IOUtils;

public class XmlToJsonMarshaller<T> extends JAXBMarshaller<T> {

    public XmlToJsonMarshaller(Class<? extends T> type) {
        this(type, null);
    }

    public XmlToJsonMarshaller(Class<? extends T> type, String rootTag) {
        super(type);
        String root = rootTag == null ? Capitalizer.firstCharToLowerCase(type.getSimpleName()) : rootTag;
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
                    XmlToJsonMarshaller.super.marshall(fobject, pwriter);
                } catch (Throwable t) {
                    throw new RuntimeException("Marshalling error", t);
                } finally {
                    IOUtils.close(pwriter);
                }
            }
        });
        PipedReader preader = new PipedReader(pwriter);
        try {
            transformer.fromXmlToJson(preader, writer);
        } finally {
            IOUtils.close(preader);
        }
    }

    private JsonToXmlTransformer transformer;
}

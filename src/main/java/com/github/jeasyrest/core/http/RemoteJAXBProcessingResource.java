package com.github.jeasyrest.core.http;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.io.Channel;
import com.github.jeasyrest.io.util.IOUtils;
import com.github.jeasyrest.xml.util.JAXBContexts;

public class RemoteJAXBProcessingResource<Req, Res> extends RemoteResource
        implements IObjectProcessingResource<Req, Res> {

    public RemoteJAXBProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        super("UTF-8");
        try {
            unmarshaller = JAXBContexts.get(requestType).createUnmarshaller();
            marshaller = JAXBContexts.get(responseType).createMarshaller();
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void process(Reader reader, Writer writer) throws IOException {
        IOUtils.write(reader, true, channel.getWriter(), true);
        IOUtils.write(channel.getReader(), true, writer, true);
    }

    @Override
    public Res process(Req request) {
        try {
            marshall(request, channel.getWriter());
            return unmarshall(channel.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void marshall(Req request, Writer writer) throws IOException {
        try {
            marshaller.marshal(request, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Res unmarshall(Reader reader) throws IOException {
        try {
            return (Res) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    private Channel channel;

}

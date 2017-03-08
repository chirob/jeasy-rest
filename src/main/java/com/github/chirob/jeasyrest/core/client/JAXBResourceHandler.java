package com.github.chirob.jeasyrest.core.client;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.chirob.jeasyrest.core.Resource;

public class JAXBResourceHandler<Req, Res> extends ResourceHandler<Req, Res> {

    public JAXBResourceHandler(String resourcePath) {
        super(resourcePath);
    }

    public JAXBResourceHandler(Resource resource) {
        super(resource);
    }
    
    @Override
    protected void marshall(Req request, Writer writer) throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(request.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
            // true);
            jaxbMarshaller.marshal(request, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Res unmarshall(Class<Res> responseType, Reader reader) throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(responseType);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Res) jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

}

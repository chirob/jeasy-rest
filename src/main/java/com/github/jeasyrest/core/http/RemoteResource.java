package com.github.jeasyrest.core.http;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IMarshaller;
import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.IUnmarshaller;
import com.github.jeasyrest.core.impl.CheckEmptyUnmarshaller;
import com.github.jeasyrest.core.impl.ProcessingResource;
import com.github.jeasyrest.core.io.WrapperChannel;
import com.github.jeasyrest.core.json.JsonToXmlUnmarshaller;
import com.github.jeasyrest.core.json.XmlToJsonMarshaller;
import com.github.jeasyrest.core.xml.JAXBMarshaller;
import com.github.jeasyrest.core.xml.JAXBUnmarshaller;
import com.github.jeasyrest.io.util.IOUtils;

public class RemoteResource<Req, Res> extends ProcessingResource implements IObjectProcessingResource<Req, Res> {

    @Override
    public IChannel openChannel(Method method) throws IOException {
        return new RemoteChannel(this, method);
    }

    public RemoteResource(String requestClassName, String requestRootTag, String responseClassName,
            String responseRootTag) {
        if (requestClassName != null) {
            Class<? extends Req> requestClass = getClass(requestClassName);
            xmlMarshaller = new JAXBMarshaller<Req>(requestClass);
            jsonMarshaller = new XmlToJsonMarshaller<Req>(requestClass, requestRootTag);
        }
        if (responseClassName != null) {
            Class<? extends Res> responseClass = getClass(responseClassName);
            xmlUnmarshaller = new CheckEmptyUnmarshaller<Res>(new JAXBUnmarshaller<Res>(responseClass));
            jsonUnmarshaller = new CheckEmptyUnmarshaller<Res>(
                    new JsonToXmlUnmarshaller<Res>(responseClass, responseRootTag));
        }
    }

    public RemoteResource(String requestClassName, String responseClassName) {
        this(requestClassName, null, responseClassName, null);
    }

    public RemoteResource() throws IOException {
        this(null, null, null, null);
    }

    @Override
    public void process(Reader reader, Writer writer, Method method) throws IOException {
        IChannel channel = null;
        try {
            channel = getRemoteChannel(method);
            if (reader != null) {
                IOUtils.write(reader, true, channel.getWriter(), true);
            }
            if (writer != null) {
                IOUtils.write(channel.getReader(), true, writer, true);
            }
        } finally {
            if (reader != null) {
                IOUtils.close(channel.getWriter());
            }
            if (writer != null) {
                IOUtils.close(channel.getReader());
            }
            try {
                channel.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public Res process(Req request, Method method) {
        Reader reader = null;
        Writer writer = null;
        IChannel channel = null;
        try {
            channel = getChannel(method);
            if (request != null) {
                writer = channel.getWriter();
                if (writer != null) {
                    marshaller.marshall(request, writer);
                }
            }
            reader = channel.getReader();
            if (reader == null) {
                return null;
            } else {
                return unmarshaller.unmarshall(reader);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(reader, writer);
            try {
                channel.close();
            } catch (IOException e) {
            }
        }
    }

    public IChannel currentChannel(Method method) throws IOException {
        IChannel channel = currentChannel();
        if (channel == null) {
            channel = getChannel(method);
        }
        return channel;
    }

    public RemoteChannel getRemoteChannel(Method method) throws IOException {
        IChannel channel = currentChannel(method);
        if (channel instanceof WrapperChannel) {
            channel = ((WrapperChannel) channel).unwrap();
        }
        return (RemoteChannel) channel;
    }

    protected void setXmlMarshall() {
        marshaller = xmlMarshaller;
        unmarshaller = xmlUnmarshaller;
    }

    protected void setJsonMarshall() {
        marshaller = jsonMarshaller;
        unmarshaller = jsonUnmarshaller;
    }

    private IMarshaller<Req> marshaller;
    private IUnmarshaller<Res> unmarshaller;
    private IMarshaller<Req> jsonMarshaller;
    private IUnmarshaller<Res> jsonUnmarshaller;
    private IMarshaller<Req> xmlMarshaller;
    private IUnmarshaller<Res> xmlUnmarshaller;

    @SuppressWarnings("unchecked")
    private static final <T> Class<T> getClass(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}

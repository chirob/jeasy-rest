package com.github.jeasyrest.core.json;

import java.io.IOException;
import java.util.Iterator;

import com.github.jeasyrest.core.IChannel;
import com.github.jeasyrest.core.IHeaders;
import com.github.jeasyrest.core.impl.ObjectProcessingResource;
import com.github.jeasyrest.core.process.json.JsonToXmlUnmarshaller;
import com.github.jeasyrest.core.process.json.XmlToJsonMarshaller;
import com.github.jeasyrest.core.process.xml.JAXBMarshaller;
import com.github.jeasyrest.core.process.xml.JAXBUnmarshaller;

public abstract class ContentTypeProcessingResource<Req, Res> extends ObjectProcessingResource<Req, Res> {

    protected ContentTypeProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        this.requestType = requestType;
        this.responseType = responseType;
    }

    @Override
    public IChannel openChannel(Method method) throws IOException {
        IChannel channel = super.openChannel(method);
        String contentType = getContentType(channel.requestHeaders());
        if (contentType != null) {
            if ("application/xml".equals(contentType)) {
                if (responseType != null) {
                    setMarshaller(new JAXBMarshaller<Res>(responseType));
                }
                if (requestType != null) {
                    setUnmarshaller(new JAXBUnmarshaller<Req>(requestType));
                }
            } else if (!"application/json".equals(contentType)) {
                throw new IllegalArgumentException("Invalid Content-Type header: [" + contentType
                        + "]; accepted values are either 'application/xml' or 'application/json'");
            }
        }
        if (responseType != null) {
            setMarshaller(new XmlToJsonMarshaller<Res>(responseType));
        }
        if (requestType != null) {
            setUnmarshaller(new JsonToXmlUnmarshaller<Req>(requestType));
        }
        return channel;
    }

    protected String getContentType(IHeaders headers) {
        if (headers == null) {
            return null;
        } else {
            Iterator<String> contentTypes = headers.get("Content-Type").iterator();
            if (contentTypes.hasNext()) {
                return contentTypes.next();
            } else {
                return "";
            }
        }
    }

    private Class<? extends Req> requestType;
    private Class<? extends Res> responseType;

}
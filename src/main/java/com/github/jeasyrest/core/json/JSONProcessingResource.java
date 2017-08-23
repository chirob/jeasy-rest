package com.github.jeasyrest.core.json;

import com.github.jeasyrest.core.impl.ObjectProcessingResource;

public abstract class JSONProcessingResource<Req, Res> extends ObjectProcessingResource<Req, Res> {

    protected JSONProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        if (responseType != null) {
            setMarshaller(new XmlToJsonMarshaller<Res>(responseType));
        }
        if (requestType != null) {
            setUnmarshaller(new JsonToXmlUnmarshaller<Req>(requestType));
        }
    }

}
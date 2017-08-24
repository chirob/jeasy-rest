package com.github.jeasyrest.core.process.xml;

import com.github.jeasyrest.core.impl.ObjectProcessingResource;

public abstract class JAXBProcessingResource<Req, Res> extends ObjectProcessingResource<Req, Res> {

    protected JAXBProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        if (responseType != null) {
            setMarshaller(new JAXBMarshaller<Res>(responseType));
        }
        if (requestType != null) {
            setUnmarshaller(new JAXBUnmarshaller<Req>(requestType));
        }
    }

}
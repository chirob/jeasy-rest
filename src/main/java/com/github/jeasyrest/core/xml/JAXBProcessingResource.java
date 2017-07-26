package com.github.jeasyrest.core.xml;

import com.github.jeasyrest.core.impl.ObjectProcessingResource;

public abstract class JAXBProcessingResource<Req, Res> extends ObjectProcessingResource<Req, Res> {

    protected JAXBProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        setMarshaller(new JAXBMarshaller<Res>(responseType));
        setUnmarshaller(new JAXBUnmarshaller<Req>(requestType));
    }

}
package com.github.jeasyrest.core.json;

import com.github.jeasyrest.core.impl.ObjectProcessingResource;

public abstract class JSONProcessingResource<Req, Res> extends ObjectProcessingResource<Req, Res> {

    protected JSONProcessingResource(Class<? extends Req> requestType, Class<? extends Res> responseType) {
        setMarshaller(new JSONMarshaller<Res>(responseType));
        setUnmarshaller(new JSONUnmarshaller<Req>(requestType));
    }

}
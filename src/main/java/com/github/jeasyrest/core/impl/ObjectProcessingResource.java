package com.github.jeasyrest.core.impl;

import com.github.jeasyrest.core.IMarshaller;
import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.IUnmarshaller;

public class ObjectProcessingResource<Req, Res> extends ProcessingResource
        implements IObjectProcessingResource<Req, Res>, ObjectProcessDelegate<Req, Res> {

    @Override
    public Res process(Req request, Method method) {
        @SuppressWarnings("unchecked")
        ObjectProcessDelegate<Req, Res> delegate = (ObjectProcessDelegate<Req, Res>) initProcessDelegate();
        return delegate.process(request, method);
    }

    @Override
    protected ProcessDelegate initProcessDelegate() {
        ProcessDelegate delegate = super.getProcessDelegate();
        if (delegate == null) {
            delegate = new DefaultObjectProcessDelegate<Req, Res>(this);
            setProcessDelegate(delegate);
        }
        return delegate;
    }
    
    protected IMarshaller<Res> getMarshaller() {
        return marshaller;
    }

    protected IObjectProcessingResource<Req, Res> setMarshaller(IMarshaller<Res> marshaller) {
        this.marshaller = marshaller;
        return this;
    }

    protected IUnmarshaller<Req> getUnmarshaller() {
        return unmarshaller;
    }

    protected IObjectProcessingResource<Req, Res> setUnmarshaller(IUnmarshaller<Req> unmarshaller) {
        this.unmarshaller = new CheckEmptyUnmarshaller<Req>(unmarshaller);
        return this;
    }

    private IMarshaller<Res> marshaller;
    private IUnmarshaller<Req> unmarshaller;

}
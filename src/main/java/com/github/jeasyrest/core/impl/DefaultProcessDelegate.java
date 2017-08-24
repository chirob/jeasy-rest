package com.github.jeasyrest.core.impl;

import com.github.jeasyrest.core.IObjectProcessingResource;
import com.github.jeasyrest.core.error.RSException;
import com.github.jeasyrest.core.error.RSException.Codes;

public class DefaultProcessDelegate<Req, Res> implements ProcessDelegate<Req, Res> {

    public Res processDelete(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled DELETE resource");
    }

    public Res processGet(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled GET resource");
    }

    public Res processOptions(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled OPTIONS resource");
    }

    public Res processPatch(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled PATCH resource");
    }

    public Res processPost(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled POST resource");
    }

    public Res processPut(Req request, IObjectProcessingResource<Req, Res> resource) {
        throw new RSException(Codes.STATUS_500, "Unhandled PUT resource");
    }

}

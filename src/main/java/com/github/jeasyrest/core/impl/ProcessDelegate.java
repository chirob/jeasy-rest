package com.github.jeasyrest.core.impl;

import com.github.jeasyrest.core.IObjectProcessingResource;

public interface ProcessDelegate<Req, Res> {

    Res processDelete(Req request, IObjectProcessingResource<Req, Res> resource);

    Res processGet(Req request, IObjectProcessingResource<Req, Res> resource);

    Res processOptions(Req request, IObjectProcessingResource<Req, Res> resource);

    Res processPatch(Req request, IObjectProcessingResource<Req, Res> resource);

    Res processPost(Req request, IObjectProcessingResource<Req, Res> resource);

    Res processPut(Req request, IObjectProcessingResource<Req, Res> resource);

}

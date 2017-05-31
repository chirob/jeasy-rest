package com.github.jeasyrest.core;

public interface IObjectProcessingResource<Req, Res> extends IProcessingResource {

    Res process(Req request);

}

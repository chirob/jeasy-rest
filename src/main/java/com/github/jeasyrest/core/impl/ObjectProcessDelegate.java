package com.github.jeasyrest.core.impl;

import com.github.jeasyrest.core.IResource.Method;

public interface ObjectProcessDelegate<Req, Res> extends ProcessDelegate {

    Res process(Req request, Method method);

}

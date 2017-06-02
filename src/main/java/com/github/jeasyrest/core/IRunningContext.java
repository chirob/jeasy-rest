package com.github.jeasyrest.core;

import com.github.jeasyrest.concurrent.util.ThreadLocalMap;

public interface IRunningContext {

    IRunningContext INSTANCE = (IRunningContext) ThreadLocalMap.INSTANCE.get(IRunningContext.class);

    <T> T getInstance(Class<T> type);

}

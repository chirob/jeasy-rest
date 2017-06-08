package com.github.jeasyrest.core;

import com.github.jeasyrest.concurrent.util.ThreadLocalMap;

public interface IRunningContext {

    IRunningContext INSTANCE = new IRunningContext() {
        public <T> T getInstance(Class<T> type) {
            return ((IRunningContext) ThreadLocalMap.getInstance().get(IRunningContext.class)).getInstance(type);
        }
    };

    <T> T getInstance(Class<T> type);

}

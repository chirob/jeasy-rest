package com.github.jeasyrest.core;

import com.github.jeasyrest.concurrent.util.ThreadLocalMap;

public interface IRunningContext {

    IRunningContext INSTANCE = new IRunningContext() {
        public <T> T getInstance(Class<T> type) {
            IRunningContext context = (IRunningContext) ThreadLocalMap.getInstance().get(IRunningContext.class);
            if (context == null) {
                return null;
            } else {
                return ((IRunningContext) ThreadLocalMap.getInstance().get(IRunningContext.class)).getInstance(type);
            }
        }
    };

    <T> T getInstance(Class<T> type);

}

package com.github.jeasyrest.core.impl;

import java.util.HashMap;
import java.util.Map.Entry;

import com.github.jeasyrest.concurrent.util.ThreadLocalMap;
import com.github.jeasyrest.core.IRunningContext;

public class RunningContext implements IRunningContext {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInstance(Class<T> type) {
        for (Entry<Class<?>, Object> entry : instances.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return (T) entry.getValue();
            }
        }
        throw new IllegalArgumentException("No instance of type " + type.getName() + " found in the execution context");
    }

    protected RunningContext put(Class<?> type, Object instance) {
        instances.put(type, instance);
        return this;
    }

    protected RunningContext() {
        ThreadLocalMap.getInstance().put(IRunningContext.class, this);
    }

    private HashMap<Class<?>, Object> instances = new HashMap<Class<?>, Object>();

}

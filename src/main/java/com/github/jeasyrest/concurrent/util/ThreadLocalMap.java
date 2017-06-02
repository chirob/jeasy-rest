package com.github.jeasyrest.concurrent.util;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ThreadLocalMap extends HashMap<Object, Object> {

    @Override
    public Object put(Object key, Object value) {
        if (containsKey(key)) {
            throw new IllegalArgumentException("Another value has already been set for key='" + key + "'");
        }
        return super.put(key, value);
    }

    private ThreadLocalMap() {
    }

    private static final ThreadLocal<ThreadLocalMap> TL = new ThreadLocal<ThreadLocalMap>() {
        @Override
        protected ThreadLocalMap initialValue() {
            return new ThreadLocalMap();
        }
    };

    public static final ThreadLocalMap INSTANCE = TL.get();

}

package com.github.chirob.jeasyrest.ioc.util;

import com.github.chirob.jeasyrest.concurrent.util.Pool;

public class PooledInstance<T> {

    public T pop(Object... initArgs) {
        if (pop) {
            pop = true;
            instance = pool.pop(initArgs);
        }
        return instance;
    }

    public void release() {
        if (pop) {
            pop = false;
            pool.release(instance);
        }
    }

    protected PooledInstance(Pool<T> pool) {
        this.pool = pool;
    }

    private Pool<T> pool;
    private T instance;
    private boolean pop;
}

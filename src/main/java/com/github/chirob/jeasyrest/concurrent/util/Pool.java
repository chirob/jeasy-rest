package com.github.chirob.jeasyrest.concurrent.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Pool<T> {

    public static final Logger logger = LoggerFactory.getLogger(Pool.class);

    public Pool(int minSize, int maxSize) {
        if (maxSize < 0) {
            throw new IllegalArgumentException("The pool size (" + maxSize + ") cannot be negative");
        }
        if (maxSize < minSize) {
            throw new IllegalArgumentException("The minimun pool size (" + minSize
                    + ") cannot be greater than maximum pool size (" + maxSize + ")");
        }
        if (maxSize != 0) {
            objects = new LinkedBlockingQueue<T>(maxSize);
            for (int i = 0; i < minSize; i++) {
                objects.offer(newInstance());
            }
        }
    }

    public T pop(Object ...initArgs) {
        if (objects != null) {
            T object = objects.poll();
            if (object != null) {
                return object;
            }
        }
        return newInstance(initArgs);
    }

    public void release(T object) {
        if (objects != null) {
            objects.offer(object);
        }
    }
    
    protected abstract T newInstance(Object ...initArgs);    
    
    private BlockingQueue<T> objects;

}

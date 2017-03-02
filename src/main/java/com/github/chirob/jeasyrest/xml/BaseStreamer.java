package com.github.chirob.jeasyrest.xml;

public abstract class BaseStreamer<T> implements Streamer<T> {

    protected String getName() {
        return name;
    }

    protected T getTarget() {
        return target;
    }

    protected BaseStreamer(String name, T target) {
        this.name = name;
        this.target = target;
    }

    private String name;
    private T target;

}

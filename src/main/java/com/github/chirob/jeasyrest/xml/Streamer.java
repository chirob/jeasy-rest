package com.github.chirob.jeasyrest.xml;

import java.io.IOException;

import com.github.chirob.jeasyrest.io.Source;

public interface Streamer<T> {

    T from(Source source) throws IOException;

    void to(Source source) throws IOException;

}

package com.github.jeasyrest.core.impl;

import java.io.IOException;
import java.io.Reader;

import com.github.jeasyrest.core.IUnmarshaller;
import com.github.jeasyrest.io.util.CheckEmptyReader;

public class CheckEmptyUnmarshaller<T> implements IUnmarshaller<T> {

    public CheckEmptyUnmarshaller(IUnmarshaller<T> unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Override
    public final T unmarshall(Reader reader) throws IOException {
        if (unmarshaller instanceof CheckEmptyUnmarshaller) {
            return (T) unmarshaller.unmarshall(reader);
        }
        if (reader != null) {
            reader = new CheckEmptyReader(reader);
            if (!((CheckEmptyReader) reader).isEmpty()) {
                return (T) unmarshaller.unmarshall(reader);
            }
        }
        return null;
    }

    private IUnmarshaller<T> unmarshaller;
}

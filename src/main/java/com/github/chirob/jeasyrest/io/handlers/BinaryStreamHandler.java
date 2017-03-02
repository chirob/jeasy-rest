package com.github.chirob.jeasyrest.io.handlers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import com.github.chirob.jeasyrest.io.StreamHandler;

public abstract class BinaryStreamHandler implements StreamHandler {

    public static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        if (sourceParams.length != 0) {
            if (sourceParams[0] instanceof Charset) {
                charset = (Charset) sourceParams[0];
            } else {
                try {
                    charset = Charset.forName(sourceParams[0].toString());
                } catch (UnsupportedCharsetException e) {
                }
            }
        }
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
    }

    protected Charset getCharset() {
        return charset;
    }

    private Charset charset;

}

package com.github.chirob.jeasyrest.core.io;

import java.io.IOException;

@SuppressWarnings("serial")
public class StreamUnavailableException extends IOException {

    public StreamUnavailableException(Throwable cause) {
        super(cause);
    }

    public StreamUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamUnavailableException(String message) {
        super(message);
    }

}

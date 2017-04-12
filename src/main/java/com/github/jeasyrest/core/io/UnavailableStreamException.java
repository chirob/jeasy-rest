package com.github.jeasyrest.core.io;

import java.io.IOException;

@SuppressWarnings("serial")
public class UnavailableStreamException extends IOException {

    public UnavailableStreamException(Throwable cause) {
        super(cause);
    }

    public UnavailableStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnavailableStreamException(String message) {
        super(message);
    }

}

package com.github.chirob.jeasyrest.core.error;

@SuppressWarnings("serial")
public class RSException extends RuntimeException {

    public RSException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    private int status;

}

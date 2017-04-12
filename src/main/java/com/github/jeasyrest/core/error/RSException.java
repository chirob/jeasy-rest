package com.github.jeasyrest.core.error;

@SuppressWarnings("serial")
public class RSException extends RuntimeException {

    public RSException(int status, String message) {
        super("[" + status + "] - " + message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    private int status;

}

package com.github.jeasyrest.core.error;

@SuppressWarnings("serial")
public class NotFoundException extends RSException {

    public NotFoundException(String resourcePath) {
        super(404, "The resource '" + resourcePath + " has not been found");
    }

}

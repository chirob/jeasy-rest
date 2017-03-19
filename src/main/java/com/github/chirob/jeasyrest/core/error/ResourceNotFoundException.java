package com.github.chirob.jeasyrest.core.error;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RSException {

    public ResourceNotFoundException(String resourcePath) {
        super(404, "The resource '" + resourcePath + " has not been found");
    }

}

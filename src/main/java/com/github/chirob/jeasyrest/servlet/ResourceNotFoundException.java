package com.github.chirob.jeasyrest.servlet;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String resourcePath) {
        super("The resource '" + resourcePath + " has not been found");
    }

}

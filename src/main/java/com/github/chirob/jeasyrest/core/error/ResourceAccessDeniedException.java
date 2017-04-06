package com.github.chirob.jeasyrest.core.error;

import java.util.Collection;

import com.github.chirob.jeasyrest.core.Resource.Method;

@SuppressWarnings("serial")
public class ResourceAccessDeniedException extends RSException {

    public ResourceAccessDeniedException(String resourcePath, Collection<Method> methods) {
        super(403, getMessage(resourcePath, methods));
    }

    private static String getMessage(String resourcePath, Collection<Method> methods) {
        String message = "Access to the resource '" + resourcePath + " is not alowed";
        if (methods != null && !methods.isEmpty()) {
            message += " for methods: " + methods;
        }
        return message;
    }
}
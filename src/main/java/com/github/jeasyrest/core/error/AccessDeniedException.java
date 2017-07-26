package com.github.jeasyrest.core.error;

import java.util.Collection;

import com.github.jeasyrest.core.IResource.Method;

@SuppressWarnings("serial")
public class AccessDeniedException extends RSException {

    public AccessDeniedException(String resourcePath, Collection<Method> methods) {
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

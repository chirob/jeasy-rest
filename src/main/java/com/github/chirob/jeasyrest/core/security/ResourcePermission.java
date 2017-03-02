package com.github.chirob.jeasyrest.core.security;

import java.security.Permission;

import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.Resource.Method;

@SuppressWarnings("serial")
public final class ResourcePermission extends Permission {

    public ResourcePermission(Resource resource) {
        this(resource, null);
    }

    public ResourcePermission(Resource resource, Method method) {
        super(resource.getPath().getPath());
        actions = "access" + getMethodActions(method);
    }

    @Override
    public int hashCode() {
        return 17 + getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Permission)) {
            return false;
        }
        return getName().equals(((Permission) obj).getName());
    }

    @Override
    public boolean implies(Permission permission) {
        return !(permission instanceof ResourcePermission);
    }

    @Override
    public String getActions() {
        return actions;
    }

    private static String getMethodActions(Method method) {
        if (method == null) {
            return "";
        } else {
            switch (method) {
            case DELETE:
                return ",read,write";
            case GET:
                return ",read";
            case OPTIONS:
                return ",read";
            case POST:
                return ",read,write";
            case PUT:
                return ",read,write";
            default:
                throw new IllegalArgumentException("Invalid REST method: " + method);
            }
        }
    }

    private String actions;

}

package com.github.chirob.jeasyrest.core.security;

import java.security.AccessController;
import java.security.Permission;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import javax.security.auth.Subject;

import com.github.chirob.jeasyrest.core.Resource.Method;

@SuppressWarnings("serial")
public final class ResourcePermission extends Permission {

    public ResourcePermission(String resourcePath) {
        this(resourcePath, null, null);
    }

    public ResourcePermission(String resourcePath, Collection<Method> methods) {
        this(resourcePath, null, methods);
    }

    public ResourcePermission(String resourcePath, Set<? extends Principal> principals) {
        this(resourcePath, principals, null);
    }

    public ResourcePermission(String resourcePath, Set<? extends Principal> principals, Collection<Method> methods) {
        super(resourcePath);
        this.principals = principals;
        this.methods = methods;
        actions = getMethodActions(methods);

    }

    @Override
    public boolean implies(Permission permission) {
        if (permission instanceof ResourcePermission) {
            boolean implies = getName().equals(permission.getName());
            if (implies) {
                if (!(principals == null || principals.isEmpty())) {
                    Subject subject = Subject.getSubject(AccessController.getContext());
                    if (subject == null) {
                        return false;
                    } else {
                        Set<Principal> subjectPrincipals = subject.getPrincipals();
                        boolean hasPrincipal = subjectPrincipals.isEmpty();
                        for (Principal principal : subjectPrincipals) {
                            hasPrincipal = hasPrincipal || principals.contains(principal);
                        }
                        implies = hasPrincipal;
                    }
                }
                if (implies) {
                    if (!(methods == null || methods.isEmpty())) {
                        Collection<Method> permissionMethods = ((ResourcePermission) permission).methods;
                        implies = permissionMethods != null && methods.containsAll(permissionMethods);
                    }
                }
            }
            return implies;
        } else {
            return false;
        }
    }

    @Override
    public String getActions() {
        return actions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
        result = prime * result + ((principals == null) ? 0 : principals.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourcePermission other = (ResourcePermission) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        if (principals == null) {
            if (other.principals != null)
                return false;
        } else if (!principals.equals(other.principals))
            return false;
        return true;
    }

    private static String getMethodActions(Collection<Method> methods) {
        if (methods == null) {
            return "read";
        } else {
            if (methods.contains(Method.DELETE) || methods.contains(Method.POST) || methods.contains(Method.PUT)) {
                return "read,write";
            } else {
                return "read";
            }
        }
    }

    private String actions;
    private Collection<Method> methods;
    private Set<? extends Principal> principals;

}

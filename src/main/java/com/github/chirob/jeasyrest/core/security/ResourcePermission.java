package com.github.chirob.jeasyrest.core.security;

import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.Permission;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.SubjectDomainCombiner;

import com.github.chirob.jeasyrest.core.Resource.Method;

@SuppressWarnings("serial")
public final class ResourcePermission extends Permission {

    public ResourcePermission(String resourcePath) {
        this(resourcePath, null, null);
    }

    public ResourcePermission(String resourcePath, Collection<Method> methods) {
        this(resourcePath, null, methods);
    }

    public ResourcePermission(String resourcePath, Set<Principal> principals) {
        this(resourcePath, principals, null);
    }

    public ResourcePermission(String resourcePath, Set<Principal> principals, Collection<Method> methods) {
        super(resourcePath);
        if (principals == null) {
            this.principals = currentPrincipals();
        } else {
            this.principals = principals;
        }

        if (principals == null) {
            this.methods = Arrays.asList(Method.values());
        } else {
            this.methods = methods;
        }
        actions = getMethodActions(methods);

    }

    @Override
    public boolean implies(Permission permission) {
        if (permission instanceof ResourcePermission) {
            boolean implies = getName().equals(permission.getName());
            if (implies) {
                Subject subject = null;
                DomainCombiner dc = AccessController.getContext().getDomainCombiner();
                if (dc instanceof SubjectDomainCombiner) {
                    subject = ((SubjectDomainCombiner) dc).getSubject();
                }
                if (subject != null) {
                    Set<Principal> principals = subject.getPrincipals();
                    for (Principal principal : principals) {
                        implies = implies || this.principals.contains(principal);
                    }
                }
                if (implies) {
                    implies = methods.containsAll(((ResourcePermission) permission).methods);
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

    private static Set<Principal> currentPrincipals() {
        Subject subject = Subject.getSubject(AccessController.getContext());
        if (subject == null) {
            return new HashSet<Principal>();
        } else {
            return subject.getPrincipals();
        }
    }

    private String actions;
    private Collection<Method> methods;
    private Set<Principal> principals;

}

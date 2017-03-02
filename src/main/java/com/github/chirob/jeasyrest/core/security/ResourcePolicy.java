package com.github.chirob.jeasyrest.core.security;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.security.SecurityPermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.AuthPermission;

public class ResourcePolicy extends Policy {

    public static final ResourcePolicy INSTANCE = new ResourcePolicy();

    public static void initialize() {
    }

    @Override
    public Provider getProvider() {
        return defaultPolicy.getProvider();
    }

    @Override
    public String getType() {
        return defaultPolicy.getType();
    }

    @Override
    public Parameters getParameters() {
        return defaultPolicy.getParameters();
    }

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
        return defaultPolicy.getPermissions(codesource);
    }

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        return defaultPolicy.getPermissions(domain);
    }

    @Override
    public boolean implies(ProtectionDomain domain, Permission permission) {
        if (permission instanceof ResourcePermission) {
            for (ResourcePermission rp : resourcePermissions) {
                if (rp.implies(permission)) {
                    return true;
                }
            }
        }
        if (grantedSecurityPermissions.contains(permission)) {
            return true;
        }
        return defaultPolicy.implies(domain, permission);
    }

    @Override
    public void refresh() {
        defaultPolicy.refresh();
    }

    private ResourcePolicy() {
        synchronized (Policy.class) {
            defaultPolicy = Policy.getPolicy();
            Policy.setPolicy(this);
            System.setSecurityManager(new SecurityManager());
        }
        resourcePermissions = new HashSet<ResourcePermission>();
        grantedSecurityPermissions = Arrays.asList(new SecurityPermission("setPolicy"),
                new AuthPermission("getSubject"));
    }

    private Policy defaultPolicy;
    private Set<ResourcePermission> resourcePermissions;
    private List<? extends Permission> grantedSecurityPermissions;

}

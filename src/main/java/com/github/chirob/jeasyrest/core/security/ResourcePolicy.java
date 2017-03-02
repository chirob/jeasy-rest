package com.github.chirob.jeasyrest.core.security;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.util.HashSet;
import java.util.Set;

public class ResourcePolicy extends Policy {

    public static final ResourcePolicy INSTANCE = new ResourcePolicy();

    public static void initialize() {
    }

    @Override
    public Provider getProvider() {
        return super.getProvider();
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public Parameters getParameters() {
        return super.getParameters();
    }

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
        return super.getPermissions(codesource);
    }

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        return super.getPermissions(domain);
    }

    @Override
    public boolean implies(ProtectionDomain domain, Permission permission) {
        Set<Permission> allPermissions = permissionStore.getAllPermissions();
        for (Permission p : allPermissions) {
            if (p.implies(permission)) {
                return true;
            }
        }
        return !(permission instanceof ResourcePermission);
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    private ResourcePolicy() {
        synchronized (Policy.class) {
            Policy.setPolicy(this);
            System.setSecurityManager(new SecurityManager());
        }
        // grantedPermissions = Arrays.asList(new AuthPermission("getSubject"));
        //// new AuthPermission("getSubject"),
        //// new RuntimePermission("accessDeclaredMembers"), new
        // FilePermission("<<ALL FILES>>", "read,write"));
    }

    private PermissionStore permissionStore = new PermissionStore() {
        @Override
        public Set<Permission> getAllPermissions() {
            return new HashSet<Permission>();
        }
    };

}

package com.github.chirob.jeasyrest.core.security;

import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.chirob.jeasyrest.core.Resource.Method;

public class ResourcePolicy extends Policy {

    public static void initialize() {
        synchronized (Policy.class) {
            Policy.setPolicy(new ResourcePolicy());
            System.setSecurityManager(new SecurityManager());
        }
    }

    @Override
    public boolean implies(ProtectionDomain domain, Permission permission) {
        if (DENY_PERMISSIONS.contains(permission)) {
            return false;
        }
        Set<Permission> allPermissions = permissionStore.getAllPermissions();
        for (Permission p : allPermissions) {
            if (p.implies(permission)) {
                return true;
            }
        }
        return !(permission instanceof ResourcePermission);
    }

    private ResourcePolicy() {
    }

    private PermissionStore permissionStore = new PermissionStore() {
        @Override
        public Set<Permission> getAllPermissions() {
            Set<Permission> allPermissions = new HashSet<Permission>();
            allPermissions.add(new ResourcePermission("/test/services/echo",
                    new HashSet<ResourcePrincipal>(Arrays.asList(new ResourcePrincipal("TEST"))),
                    new HashSet<Method>()));
            return allPermissions;
        }
    };

    private static final List<? extends Permission> DENY_PERMISSIONS = Arrays
            .asList(new RuntimePermission("setSecurityManager"), new SecurityPermission("setPolicy"));

}

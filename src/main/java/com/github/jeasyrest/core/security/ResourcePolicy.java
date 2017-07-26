package com.github.jeasyrest.core.security;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.github.jeasyrest.core.IResource.Method;
import com.github.jeasyrest.core.error.AccessDeniedException;
import com.github.jeasyrest.core.etc.Configuration;
import com.github.jeasyrest.core.security.data.PermissionStore;
import com.github.jeasyrest.ioc.Injections;

public class ResourcePolicy extends Policy {

    public static void checkPermission(String resourcePath) {
        checkPermission(resourcePath, null, null);
    }

    public static void checkPermission(String resourcePath, Collection<Method> methods) {
        checkPermission(resourcePath, null, methods);
    }

    public static void checkPermission(String resourcePath, Set<? extends Principal> principals) {
        checkPermission(resourcePath, principals, null);
    }

    public static void checkPermission(String resourcePath, Set<? extends Principal> principals,
            Collection<Method> methods) {
        if (SECURITY_ENABLED) {
            try {
                AccessController.checkPermission(new ResourcePermission(resourcePath, principals, methods));
            } catch (AccessControlException e) {
                throw new AccessDeniedException(resourcePath, methods);
            }
        }
    }

    public static void initialize() {
        if (SECURITY_ENABLED) {
            synchronized (Policy.class) {
                Policy.setPolicy(new ResourcePolicy());
                System.setSecurityManager(new SecurityManager());
            }
        }
    }

    @Override
    public boolean implies(ProtectionDomain domain, Permission permission) {
        if (SECURITY_ENABLED) {
            if (denyPermissions.contains(permission)) {
                return false;
            }
            Set<Permission> allPermissions = permissionStore.getAllPermissions();
            for (Permission p : allPermissions) {
                if (p.implies(permission)) {
                    return true;
                }
            }
            return !(permission instanceof ResourcePermission);
        } else {
            return true;
        }
    }

    private ResourcePolicy() {
        if (SECURITY_ENABLED) {
            denyPermissions = Arrays.asList(new RuntimePermission("setSecurityManager"),
                    new SecurityPermission("setPolicy"));
            permissionStore = Injections.INSTANCE.singleton("permissionStore");
        } else {
            denyPermissions = null;
            permissionStore = null;
        }
    }

    private final PermissionStore permissionStore;
    private final List<? extends Permission> denyPermissions;

    private static final boolean SECURITY_ENABLED = Configuration.INSTANCES.get("resources")
            .getBoolean("checkPermission.enabled", false);

}
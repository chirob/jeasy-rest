package com.github.chirob.jeasyrest.core.security;

import java.security.Permission;
import java.util.Set;

public interface PermissionStore {

    Set<Permission> getAllPermissions();
    
}

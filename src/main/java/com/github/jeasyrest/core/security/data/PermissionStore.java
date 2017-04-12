package com.github.jeasyrest.core.security.data;

import java.security.Permission;
import java.util.Set;

public interface PermissionStore {

    Set<Permission> getAllPermissions();
    
}

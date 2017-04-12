package com.github.jeasyrest.core.security.data;

import java.security.Permission;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.jeasyrest.core.Resource.Method;
import com.github.jeasyrest.core.security.ResourcePermission;
import com.github.jeasyrest.core.security.ResourcePrincipal;

public class GenericPermissionStore implements PermissionStore {

    @Override
    public Set<Permission> getAllPermissions() {
        return allPermissions;
    }

    protected GenericPermissionStore() {
    }

    protected GenericPermissionStore(Iterable<PermissionRecord> permissionRecords) {
        init(permissionRecords);
    }

    protected void init(Iterable<PermissionRecord> permissionRecords) {
        for (PermissionRecord permissionRecord : permissionRecords) {
            if (permissionRecord.principalRecords == null || permissionRecord.principalRecords.isEmpty()) {
                allPermissions.add(new ResourcePermission(permissionRecord.resourcePath));
            } else {
                if (permissionRecord.principalRecords != null) {
                    for (PrincipalRecord principalRecord : permissionRecord.principalRecords) {
                        Set<Principal> principalSet = new HashSet<Principal>();
                        Set<Method> methodSet = new HashSet<Method>();
                        if (principalRecord.principalName != null
                                && principalRecord.principalName.trim().length() != 0) {
                            principalSet.add(new ResourcePrincipal(principalRecord.principalName));
                        }
                        if (principalRecord.methodNames != null) {
                            for (String methodName : principalRecord.methodNames) {
                                methodSet.add(Method.valueOf(methodName.toUpperCase()));
                            }
                        }
                        allPermissions
                                .add(new ResourcePermission(permissionRecord.resourcePath, principalSet, methodSet));
                    }
                }
            }
        }
    }

    protected static final class PermissionRecord {
        protected PermissionRecord() {
        }

        protected PermissionRecord(String resourcePath, List<PrincipalRecord> principalRecords) {
            this.resourcePath = resourcePath;
            this.principalRecords = principalRecords;
        }

        protected String getResourcePath() {
            return resourcePath;
        }

        protected void setResourcePath(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        protected List<PrincipalRecord> getPrincipalRecords() {
            return principalRecords;
        }

        protected void setPrincipalRecords(List<PrincipalRecord> principalRecords) {
            this.principalRecords = principalRecords;
        }

        private String resourcePath;
        private List<PrincipalRecord> principalRecords;

    }

    protected static final class PrincipalRecord {
        protected PrincipalRecord() {
        }

        protected PrincipalRecord(String principalName, String[] methodNames) {
            this.principalName = principalName;
            this.methodNames = methodNames;
        }

        protected String getPrincipalName() {
            return principalName;
        }

        protected void setPrincipalName(String principalName) {
            this.principalName = principalName;
        }

        protected String[] getMethodNames() {
            return methodNames;
        }

        protected void setMethodNames(String[] methodNames) {
            this.methodNames = methodNames;
        }

        private String principalName;
        private String[] methodNames;
    }

    private Set<Permission> allPermissions = new HashSet<Permission>();
}

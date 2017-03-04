package com.github.chirob.jeasyrest.core.security.data;

import java.io.IOException;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.github.chirob.jeasyrest.core.Resource.Method;
import com.github.chirob.jeasyrest.core.security.ResourcePermission;
import com.github.chirob.jeasyrest.core.security.ResourcePrincipal;
import com.github.chirob.jeasyrest.io.util.LoadedProperties;
import com.github.chirob.jeasyrest.reflect.ClassloaderResources;

public class FilePermissionStore implements PermissionStore {

    @Override
    public Set<Permission> getAllPermissions() {
        return allPermissions;
    }

    public FilePermissionStore() {
        LoadedProperties resourceProps = null;
        for (String mapName : MAP_NAMES) {
            ClassloaderResources resources = new ClassloaderResources(mapName);
            for (URL url : resources) {
                try {
                    resourceProps = new LoadedProperties(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (Entry<Object, Object> entry : resourceProps.entrySet()) {
                    String resourcePath = ((String) entry.getKey()).trim();
                    String values = ((String) entry.getValue()).trim();
                    if (values == null || values.trim().length() == 0) {
                        allPermissions.add(new ResourcePermission(resourcePath));
                    } else {
                        String[] tokens = values.split(";");
                        if (tokens.length > 0) {
                            for (String token : tokens) {
                                String[] subtokens = token.split(":");
                                if (subtokens.length > 0) {
                                    Set<Principal> principalSet = new HashSet<Principal>();
                                    Set<Method> methodSet = new HashSet<Method>();
                                    String principalName = subtokens[0].trim();
                                    if (principalName.length() != 0) {
                                        principalSet.add(new ResourcePrincipal(principalName));
                                    }
                                    if (subtokens.length > 1) {
                                        String[] methodNames = subtokens[1].split(",");
                                        for (String methodName : methodNames) {
                                            methodSet.add(Method.valueOf(methodName.toUpperCase()));
                                        }
                                    }
                                    allPermissions.add(new ResourcePermission(resourcePath, principalSet, methodSet));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Set<Permission> allPermissions = new HashSet<Permission>();

    private static final String[] MAP_NAMES = { "META_INF/jeasyrest/resources.policy", "jeasyrest/resources.policy" };
}

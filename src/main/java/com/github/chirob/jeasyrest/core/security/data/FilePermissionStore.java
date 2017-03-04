package com.github.chirob.jeasyrest.core.security.data;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.chirob.jeasyrest.io.util.LoadedProperties;
import com.github.chirob.jeasyrest.reflect.ClassloaderResources;

public class FilePermissionStore extends GenericPermissionStore {

    public FilePermissionStore() {
        List<PermissionRecord> permissionRecords = new LinkedList<PermissionRecord>();
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
                    List<PrincipalRecord> principalRecords = new LinkedList<PrincipalRecord>();
                    String resourcePath = ((String) entry.getKey()).trim();
                    String values = ((String) entry.getValue()).trim();
                    if (values != null && values.trim().length() != 0) {
                        String principalName = null;
                        String[] methodNames = null;
                        String[] tokens = values.split(";");
                        if (tokens.length != 0) {
                            for (String token : tokens) {
                                String[] subtokens = token.split(":");
                                if (subtokens.length > 0) {
                                    principalName = subtokens[0].trim();
                                    if (subtokens.length > 1) {
                                        methodNames = subtokens[1].split(",");
                                    }
                                    principalRecords.add(new PrincipalRecord(principalName, methodNames));
                                }
                            }
                        }
                    }
                    permissionRecords.add(new PermissionRecord(resourcePath, principalRecords));
                }
            }
        }

        init(permissionRecords);
    }

    private static final String[] MAP_NAMES = { "META_INF/jeasyrest/resources.policy", "jeasyrest/resources.policy" };
}

package com.github.jeasyrest.core.impl;

import java.net.URI;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map.Entry;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.security.ResourcePolicy;
import com.github.jeasyrest.ioc.util.InjectionMap;
import com.github.jeasyrest.reflect.InstanceConstructor;

class ResourceMap extends InjectionMap {

    <T extends IResource> T get(String resourcePath) {
        URI resPath = null;
        String resPathPattern = null;
        Object[] resParameters = EMPTY_PARAMS;

        if (injectors.containsKey(resourcePath)) {
            resPathPattern = resourcePath;
        } else {
            for (String id : injectors.keySet()) {
                String pathPattern = id.replaceAll("\\{\\d+\\}", ".+");
                if (resourcePath.matches(pathPattern)) {
                    try {
                        resParameters = new MessageFormat(id).parse(resourcePath);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                    resPathPattern = pathPattern;
                }
            }
        }

        if (resPathPattern == null) {
            return null;
        } else {
            resPath = URI.create(resourcePath);
            T resource = newResourceInstance(resPathPattern);
            ((Resource) resource).init(resPath, resPathPattern, resParameters);
            return resource;
        }
    }

    ResourceMap() {
        super("META-INF/jeasyrest/resources", "jeasyrest/resources");
        ResourcePolicy.initialize();
    }

    private <T extends IResource> T newResourceInstance(String resPathPattern) {
        for (Entry<String, List<InstanceConstructor<?>>> entry : injectors.entrySet()) {
            String id = entry.getKey();
            if (id.matches(resPathPattern)) {
                return newInstance(id);
            }
        }
        return null;
    }

    private static final Object[] EMPTY_PARAMS = new Object[0];
}

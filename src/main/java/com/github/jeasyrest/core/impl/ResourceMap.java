package com.github.jeasyrest.core.impl;

import java.net.URI;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.security.ResourcePolicy;
import com.github.jeasyrest.ioc.util.InjectionMap;

class ResourceMap extends InjectionMap {

    <T extends IResource> T get(String resourcePath) {
        String resPathPattern = null;
        Object[] resParameters = EMPTY_PARAMS;

        Set<String> idSet = null;
        if (injectors.containsKey(resourcePath)) {
            idSet = new HashSet<String>();
            idSet.add(resourcePath);
        } else {
            idSet = injectors.keySet();
        }
        String resId = null;
        for (String id : idSet) {
            String pathPattern = id.replaceAll("\\{\\d+\\}", ".+");
            if (resourcePath.matches(pathPattern)) {
                try {
                    resParameters = new MessageFormat(id).parse(resourcePath);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
                resPathPattern = pathPattern;
                resId = id;
                break;
            }
        }

        if (resPathPattern == null) {
            return null;
        } else {
            URI resPath = URI.create(resourcePath);
            T resource = newInstance(resId);
            ((Resource) resource).init(resPath, resPathPattern, resParameters);
            return resource;
        }

    }

    ResourceMap() {
        super("META-INF/jeasyrest/resources", "jeasyrest/resources");
        ResourcePolicy.initialize();
    }

    private static final Object[] EMPTY_PARAMS = new Object[0];
}

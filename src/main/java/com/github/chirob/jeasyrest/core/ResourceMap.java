package com.github.chirob.jeasyrest.core;

import java.net.URI;
import java.text.MessageFormat;
import java.text.ParseException;

import com.github.chirob.jeasyrest.core.security.ResourcePolicy;
import com.github.chirob.jeasyrest.ioc.util.InjectionMap;

class ResourceMap extends InjectionMap {

    <T extends Resource> T get(String resourcePath) {
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
            T resource = newInstance(resPathPattern);
            resource.init(resPath, resPathPattern, resParameters);
            return resource;
        }
    }

    ResourceMap() {
        super("META-INF/jeasyrest/resources", "jeasyrest/resources");
        ResourcePolicy.initialize();
    }

    private static final Object[] EMPTY_PARAMS = new Object[0];
}

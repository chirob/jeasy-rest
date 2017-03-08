package com.github.chirob.jeasyrest.core;

import java.net.URI;
import java.text.MessageFormat;
import java.text.ParseException;

import com.github.chirob.jeasyrest.core.security.ResourcePolicy;
import com.github.chirob.jeasyrest.ioc.util.InjectionMap;

class ResourceMap extends InjectionMap {

    <T extends Resource> T get(String resourcePath) {
        for (String id : injectors.keySet()) {
            MessageFormat pathFormat = new MessageFormat(id);
            String pathPattern = pathFormat.toPattern().replaceAll("\\{\\d+\\}", ".+");
            if (resourcePath.matches(pathPattern)) {
                URI resPath = URI.create(resourcePath);
                String resPathPattern = pathPattern;
                Object[] resParameters;
                try {
                    resParameters = pathFormat.parse(resourcePath);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
                T resource = newInstance(id);
                resource.init(resPath, resPathPattern, resParameters);
                return resource;
            }
        }
        return null;
    }

    ResourceMap() {
        super("META-INF/jeasyrest/resources", "jeasyrest/resources");
        ResourcePolicy.initialize();
    }

}

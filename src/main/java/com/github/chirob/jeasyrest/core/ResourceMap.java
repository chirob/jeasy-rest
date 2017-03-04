package com.github.chirob.jeasyrest.core;

import java.net.URI;
import java.text.MessageFormat;
import java.text.ParseException;

import com.github.chirob.jeasyrest.core.security.ResourcePolicy;
import com.github.chirob.jeasyrest.ioc.util.InjectionMap;

class ResourceMap extends InjectionMap {

    Resource get(String resourcePath) {
        for (String id : injectors.keySet()) {
            MessageFormat pathFormat = new MessageFormat(id);
            String pathPattern = pathFormat.toPattern().replaceAll("\\{\\d+\\}", ".+");
            if (resourcePath.matches(pathPattern)) {
                Object[] params;
                try {
                    params = pathFormat.parse(resourcePath);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
                Resource resource = (Resource) newInstance(id);
                resource.path = URI.create(resourcePath);
                resource.pathPattern = pathPattern;
                resource.parameters = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    resource.setParameter(i, params[i].toString());
                }
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

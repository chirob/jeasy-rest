package com.github.chirob.jeasyrest.core;

import java.net.URI;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Map.Entry;

import com.github.chirob.jeasyrest.core.security.ResourcePolicy;
import com.github.chirob.jeasyrest.ioc.InjectionMap;
import com.github.chirob.jeasyrest.reflect.InstanceConstructor;

class ResourceMap extends InjectionMap {

    Resource get(String resourcePath) {
        for (Entry<String, InstanceConstructor<Object>> entry : injectors.entrySet()) {
            MessageFormat pathFormat = new MessageFormat(entry.getKey());
            String pathPattern = pathFormat.toPattern().replaceAll("\\{\\d+\\}", ".+");
            if (resourcePath.matches(pathPattern)) {
                InstanceConstructor<Object> resourceConstr = entry.getValue();
                Object[] params;
                try {
                    params = pathFormat.parse(resourcePath);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
                Resource resource = (Resource) resourceConstr.newInstance();
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
        super("jeasyrest/resources", "jeasyrest/resources.override");
        ResourcePolicy.initialize();
    }

}

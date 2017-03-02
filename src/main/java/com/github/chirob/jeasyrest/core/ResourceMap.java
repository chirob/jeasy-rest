package com.github.chirob.jeasyrest.core;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import com.github.chirob.jeasyrest.core.security.ResourcePolicy;
import com.github.chirob.jeasyrest.io.util.LoadedProperties;
import com.github.chirob.jeasyrest.reflect.ClassloaderResources;
import com.github.chirob.jeasyrest.reflect.InstanceConstructor;

class ResourceMap {

    Resource get(String resourcePath) {
        for (Entry<MessageFormat, InstanceConstructor<Resource>> entry : resourceMap.entrySet()) {
            MessageFormat pathFormat = entry.getKey();
            String pathPattern = pathFormat.toPattern().replaceAll("\\{\\d+\\}", ".+");
            InstanceConstructor<Resource> resourceConstr = RESOURCE_CACHE.get(pathPattern);
            if (resourceConstr == null) {
                if (resourcePath.matches(pathPattern)) {
                    resourceConstr = entry.getValue();
                    RESOURCE_CACHE.put(pathPattern, resourceConstr);
                }
            }
            Object[] params;
            try {
                params = pathFormat.parse(resourcePath);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
            Resource resource = resourceConstr.newInstance();
            resource.path = URI.create(resourcePath);
            resource.parameters = new String[params.length];
            for (int i = 0; i < params.length; i++) {
                resource.setParameter(i, params[i].toString());
            }
            return resource;
        }
        return null;
    }

    ResourceMap() {
        resourceMap = new HashMap<MessageFormat, InstanceConstructor<Resource>>();
        init("jeasyrest/resources");
        init("jeasyrest/resources.override");
        
        ResourcePolicy.initialize();        
    }

    @SuppressWarnings("unchecked")
    private void init(String handlersResourceName) {
        LoadedProperties resourceProps = null;
        ClassloaderResources resources = new ClassloaderResources(handlersResourceName);
        for (URL url : resources) {
            try {
                resourceProps = new LoadedProperties(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (Entry<Object, Object> entry : resourceProps.entrySet()) {
                String name = (String) entry.getKey();
                String values = (String) entry.getValue();
                if (values != null && values.trim().length() != 0) {
                    Object[] tokens = values.split(",");
                    Class<? extends Resource> resourceType;
                    try {
                        resourceType = (Class<? extends Resource>) Class.forName((String) tokens[0]);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                    Object[] initArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                    try {
                        InstanceConstructor<Resource> resourceConstr = new InstanceConstructor<Resource>(resourceType,
                                initArgs);
                        resourceMap.put(new MessageFormat(name), resourceConstr);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Cannot create resource '" + name + "'", e);
                    }
                }
            }
        }
    }

    private HashMap<MessageFormat, InstanceConstructor<Resource>> resourceMap;

    private static final HashMap<String, InstanceConstructor<Resource>> RESOURCE_CACHE = new HashMap<String, InstanceConstructor<Resource>>();
}

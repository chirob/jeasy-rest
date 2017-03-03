package com.github.chirob.jeasyrest.ioc;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.chirob.jeasyrest.io.util.LoadedProperties;
import com.github.chirob.jeasyrest.reflect.ClassloaderResources;
import com.github.chirob.jeasyrest.reflect.InstanceConstructor;

public class InjectionMap {

    public InjectionMap() {
    }

    public InjectionMap(String... mapNames) {
        init(mapNames);
    }

    public <T> T getSingleton(String id) {
        @SuppressWarnings("unchecked")
        T singleton = (T) singletons.get(id);
        if (singleton == null) {
            singleton = getNewInstance(id);
        }
        return singleton;
    }

    @SuppressWarnings("unchecked")
    public <T> T getNewInstance(String id) {
        return (T) injectors.get(id);
    }

    protected void init(String... mapNames) {
        LoadedProperties resourceProps = null;
        for (String mapName : mapNames) {
            ClassloaderResources resources = new ClassloaderResources(mapName);
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
                        Class<?> objectType;
                        try {
                            objectType = Class.forName((String) tokens[0]);
                        } catch (ClassNotFoundException e) {
                            throw new IllegalArgumentException(e);
                        }
                        Object[] initArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                        try {
                            InstanceConstructor<Object> objectConstr = new InstanceConstructor<Object>(objectType,
                                    initArgs);
                            injectors.put(name, objectConstr);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Cannot create resource '" + name + "'", e);
                        }
                    }
                }
            }
        }
    }

    protected Map<String, InstanceConstructor<Object>> injectors = new HashMap<String, InstanceConstructor<Object>>();
    protected Map<String, Object> singletons = new HashMap<String, Object>();

}

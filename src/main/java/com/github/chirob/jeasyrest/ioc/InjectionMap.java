package com.github.chirob.jeasyrest.ioc;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chirob.jeasyrest.io.util.LoadedProperties;
import com.github.chirob.jeasyrest.reflect.ClassloaderResources;
import com.github.chirob.jeasyrest.reflect.InstanceConstructor;

public class InjectionMap {

    private static final Logger logger = LoggerFactory.getLogger(InjectionMap.class);

    public InjectionMap() {
    }

    public InjectionMap(String... mapNames) {
        init(mapNames);
    }

    @SuppressWarnings("unchecked")
    public <T> T singleton(String id, Object... initArgs) {
        return (T) singletons(id, initArgs).get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstance(String id, Object... initArgs) {
        return (T) newInstances(id, initArgs).get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> singletons(String id, Object... initArgs) {
        List<T> singletonList = (List<T>) singletons.get(id);
        if (singletonList == null) {
            singletonList = new LinkedList<T>();
            singletonList.addAll((List<T>) newInstances(id, initArgs));
        }
        return singletonList;
    }

    public <T> List<T> newInstances(String id, Object... initArgs) {
        return newInstances(id, injectors.get(id), initArgs);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> newInstances(String id, List<InstanceConstructor<?>> constrList, Object... initArgs) {
        List<T> newInstanceList = new LinkedList<T>();
        for (InstanceConstructor<?> constr : constrList) {
            try {
                logger.debug("Tryinh to construct object identifyed by \"" + id + "\"");
                newInstanceList.add((T) constr.newInstance(initArgs));
                logger.debug("Construction object succeed");
            } catch (Exception e) {
                logger.debug("Construction object failed");
            }
        }
        return newInstanceList;
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
                        List<InstanceConstructor<?>> objectConstrList = injectors.get(name);
                        if (objectConstrList == null) {
                            objectConstrList = new LinkedList<InstanceConstructor<?>>();
                            injectors.put(name, objectConstrList);
                        }
                        Object[] initArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                        try {
                            InstanceConstructor<Object> objectConstr = new InstanceConstructor<Object>(objectType,
                                    initArgs);
                            objectConstrList.add(objectConstr);
                        } catch (Exception e) {
                            throw new IllegalArgumentException(
                                    "Cannot create constructor for \"" + name + "\" identifier'", e);
                        }
                    }
                }
            }
        }
    }

    protected Map<String, List<InstanceConstructor<?>>> injectors = new HashMap<String, List<InstanceConstructor<?>>>();
    protected Map<String, List<?>> singletons = new HashMap<String, List<?>>();

}

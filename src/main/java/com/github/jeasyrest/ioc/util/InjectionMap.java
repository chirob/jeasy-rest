package com.github.jeasyrest.ioc.util;

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

import com.github.jeasyrest.concurrent.util.Pool;
import com.github.jeasyrest.io.util.LoadedProperties;
import com.github.jeasyrest.reflect.ClassloaderResources;
import com.github.jeasyrest.reflect.InstanceConstructor;

public class InjectionMap {

    private static final Logger logger = LoggerFactory.getLogger(InjectionMap.class);

    public <T> PooledInstance<T> pooledInstance(String id) {
        List<PooledInstance<T>> pooledInstances = pooledInstances(id);
        if (pooledInstances.isEmpty()) {
            throw new IllegalArgumentException("Cannot retrieve pooled instance by identifier: \"" + id + "\"");
        } else {
            return pooledInstances.get(0);
        }
    }

    public <T> Pool<T> poolInstance(String id, Object... initArgs) {
        List<Pool<T>> poolInstances = poolInstances(id);
        if (poolInstances.isEmpty()) {
            throw new IllegalArgumentException("Cannot retrieve pool instance by identifier: \"" + id + "\"");
        } else {
            return poolInstances.get(0);
        }
    }

    public <T> T newInstance(String id, Object... initArgs) {
        List<T> newInstances = newInstances(id, initArgs);
        if (newInstances.isEmpty()) {
            throw new IllegalArgumentException("Cannot retrieve new instance by identifier: \"" + id + "\"");
        } else {
            return (T) newInstances.get(0);
        }
    }

    public <T> T singleton(String id, Object... initArgs) {
        List<T> singletons = singletons(id, initArgs);
        if (singletons.isEmpty()) {
            throw new IllegalArgumentException("Cannot retrieve singleton instance by identifier: \"" + id + "\"");
        } else {
            return (T) singletons.get(0);
        }
    }

    protected InjectionMap(String... mapNames) {
        init(mapNames);
    }

    protected <T> List<T> newInstances(String id, Object... initArgs) {
        return newInstances(id, injectors.get(id), initArgs);
    }

    protected <T> List<PooledInstance<T>> pooledInstances(String id) {
        List<Pool<T>> poolInstances = poolInstances(id);
        List<PooledInstance<T>> pooledInstances = new LinkedList<PooledInstance<T>>();
        for (Pool<T> pool : poolInstances) {
            pooledInstances.add(new PooledInstance<T>(pool));
        }
        return pooledInstances;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<Pool<T>> poolInstances(String id) {
        List<Pool<T>> poolList = (List<Pool<T>>) pools.get(id);
        if (poolList == null) {
            final String finalId = id;
            poolList = new LinkedList<Pool<T>>();
            poolList.add(new Pool<T>(0, 20) {
                protected T newInstance(Object... initArgs) {
                    return (T) InjectionMap.this.newInstance(finalId, initArgs);
                }
            });
        }
        return poolList;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> singletons(String id, Object... initArgs) {
        List<T> singletonList = (List<T>) singletons.get(id);
        if (singletonList == null) {
            singletonList = new LinkedList<T>();
            singletonList.addAll((List<T>) newInstances(id, initArgs));
        }
        return singletonList;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> newInstances(String id, List<InstanceConstructor<?>> constrList, Object... initArgs) {
        List<T> newInstanceList = new LinkedList<T>();
        for (InstanceConstructor<?> constr : constrList) {
            try {
                logger.trace("Trying to construct object identifyed by \"" + id + "\"");
                newInstanceList.add((T) constr.newInstance(initArgs));
                logger.trace("Construction object succeed");
            } catch (Throwable t) {
                logger.trace("Construction object failed", t);
                if (constrList.size() == 1) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else {
                        throw new RuntimeException(t);
                    }
                }
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
                        } catch (Exception e) {
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
    protected Map<String, List<? extends Pool<?>>> pools = new HashMap<String, List<? extends Pool<?>>>();

}

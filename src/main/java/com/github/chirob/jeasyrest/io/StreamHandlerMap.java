package com.github.chirob.jeasyrest.io;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.chirob.jeasyrest.ioc.InjectionMap;
import com.github.chirob.jeasyrest.reflect.InstanceConstructor;
import com.github.chirob.jeasyrest.reflect.TypeHierarchy;

class StreamHandlerMap extends InjectionMap {

    @SuppressWarnings("unchecked")
    Set<InstanceConstructor<? extends StreamHandler>> get(Class<?> type) {
        synchronized (this) {
            Set<InstanceConstructor<? extends StreamHandler>> handlerConstructors = instanceHandlerMap.get(type);
            if (handlerConstructors == null) {
                handlerConstructors = new HashSet<InstanceConstructor<? extends StreamHandler>>();
                TypeHierarchy typeHierarchy = new TypeHierarchy(type);
                for (Entry<String, InstanceConstructor<Object>> entry : injectors.entrySet()) {
                    Class<? extends StreamHandler> handlerType;
                    try {
                        handlerType = (Class<? extends StreamHandler>) Class.forName(entry.getKey());
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                    if (typeHierarchy.contains(handlerType)) {
                        handlerConstructors.add((InstanceConstructor<? extends StreamHandler>) entry.getValue());
                    }
                }
                instanceHandlerMap.put(type, Collections.unmodifiableSet(handlerConstructors));
            }
            return handlerConstructors;
        }
    }

    StreamHandlerMap() {
        instanceHandlerMap = new HashMap<Class<?>, Set<InstanceConstructor<? extends StreamHandler>>>();
        init("jeasyrest/stream.handlers.override", "jeasyrest/stream.handlers");
    }

    private Map<Class<?>, Set<InstanceConstructor<? extends StreamHandler>>> instanceHandlerMap;

}

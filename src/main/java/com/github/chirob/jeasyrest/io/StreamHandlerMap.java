package com.github.chirob.jeasyrest.io;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.chirob.jeasyrest.ioc.InjectionMap;
import com.github.chirob.jeasyrest.reflect.InstanceConstructor;
import com.github.chirob.jeasyrest.reflect.TypeHierarchy;

class StreamHandlerMap extends InjectionMap {

    @SuppressWarnings("unchecked")
    List<? extends StreamHandler> get(Class<?> type) {
        synchronized (this) {
            List<InstanceConstructor<?>> handlerConstructors = instanceHandlerMap.get(type);
            if (handlerConstructors == null) {
                handlerConstructors = new LinkedList<InstanceConstructor<?>>();
                TypeHierarchy typeHierarchy = new TypeHierarchy(type);
                for (String id : injectors.keySet()) {
                    Class<? extends StreamHandler> handlerType;
                    try {
                        handlerType = (Class<? extends StreamHandler>) Class.forName(id);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                    if (typeHierarchy.contains(handlerType)) {
                        handlerConstructors.addAll(injectors.get(id));
                    }
                }
                instanceHandlerMap.put(type, handlerConstructors);
            }
            return newInstances(type.getName(), handlerConstructors);
        }
    }

    StreamHandlerMap() {
        instanceHandlerMap = new HashMap<Class<?>, List<InstanceConstructor<?>>>();
        init("META-INF/jeasyrest/stream.handlers", "jeasyrest/stream.handlers");
    }

    private Map<Class<?>, List<InstanceConstructor<?>>> instanceHandlerMap;

}

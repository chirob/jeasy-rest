package com.github.jeasyrest.io;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.jeasyrest.ioc.util.InjectionMap;
import com.github.jeasyrest.reflect.InstanceConstructor;
import com.github.jeasyrest.reflect.TypeHierarchy;

class StreamHandlerMap extends InjectionMap {

    List<? extends StreamHandler> get(Class<?> type) {
        synchronized (this) {
            List<InstanceConstructor<?>> handlerConstructors = instanceHandlerMap.get(type);
            if (handlerConstructors == null) {
                handlerConstructors = new LinkedList<InstanceConstructor<?>>();
                TypeHierarchy typeHierarchy = new TypeHierarchy(type);
                for (String id : injectors.keySet()) {
                    Class<?> c = null;
                    try {
                        c = Class.forName(id);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                    if (typeHierarchy.contains(c)) {
                        handlerConstructors.addAll(injectors.get(id));
                    }
                }
                instanceHandlerMap.put(type, handlerConstructors);
            }
            return newInstances(type.getName(), handlerConstructors);
        }
    }

    StreamHandlerMap() {
        super("META-INF/jeasyrest/stream.handlers", "jeasyrest/stream.handlers");
    }

    private Map<Class<?>, List<InstanceConstructor<?>>> instanceHandlerMap = new HashMap<Class<?>, List<InstanceConstructor<?>>>();;

}

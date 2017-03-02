package com.github.chirob.jeasyrest.io;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.github.chirob.jeasyrest.io.util.LoadedProperties;
import com.github.chirob.jeasyrest.reflect.ClassloaderResources;
import com.github.chirob.jeasyrest.reflect.TypeHierarchy;

class StreamHandlerMap {

    Set<Class<? extends StreamHandler>> get(Class<?> type) {
        synchronized (this) {
            Set<Class<? extends StreamHandler>> streamHandlers = instanceHandlerMap.get(type);
            if (streamHandlers == null) {
                streamHandlers = new HashSet<Class<? extends StreamHandler>>();
                TypeHierarchy typeHierarchy = new TypeHierarchy(type);
                for (Entry<Class<?>, Set<Class<? extends StreamHandler>>> entry : handlerMap.entrySet()) {
                    if (typeHierarchy.contains(entry.getKey())) {
                        streamHandlers.addAll(entry.getValue());
                    }
                }
                instanceHandlerMap.put(type, Collections.unmodifiableSet(streamHandlers));
            }
            return streamHandlers;
        }
    }

    StreamHandlerMap() {
        handlerMap = new HashMap<Class<?>, Set<Class<? extends StreamHandler>>>();
        instanceHandlerMap = new HashMap<Class<?>, Set<Class<? extends StreamHandler>>>();
        init("jeasyrest/stream.handlers.override");
        init("jeasyrest/stream.handlers");
    }

    @SuppressWarnings("unchecked")
    private void init(String handlersResourceName) {
        LoadedProperties streamHandlerProps = null;
        ClassloaderResources resources = new ClassloaderResources(handlersResourceName);
        for (URL url : resources) {
            try {
                streamHandlerProps = new LoadedProperties(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (Entry<Object, Object> entry : streamHandlerProps.entrySet()) {
                String key = (String) entry.getKey();
                Class<?> keyType;
                try {
                    keyType = Class.forName(key);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
                Set<Class<? extends StreamHandler>> handlers = handlerMap.get(keyType);
                if (handlers == null) {
                    handlers = new HashSet<Class<? extends StreamHandler>>();
                    handlerMap.put(keyType, handlers);
                }
                String values = (String) entry.getValue();
                if (values != null && values.trim().length() != 0) {
                    for (String value : values.split(",")) {
                        Class<? extends StreamHandler> shClass;
                        try {
                            shClass = (Class<? extends StreamHandler>) Class.forName(value);
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                        handlers.add(shClass);
                    }
                }
            }
        }
    }

    private HashMap<Class<?>, Set<Class<? extends StreamHandler>>> instanceHandlerMap;

    private HashMap<Class<?>, Set<Class<? extends StreamHandler>>> handlerMap;
}

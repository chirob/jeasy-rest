package com.github.jeasyrest.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class InstanceConstructor<T> {

    public InstanceConstructor(Class<? extends T> type, Object... initArgs) {
        addConstructors(type.getConstructors());
        addConstructors(type.getDeclaredConstructors());
        initArgsList = Arrays.asList(initArgs);
    }

    public boolean handleType(Class<?> type) {
        for (Constructor<T> c : constructors) {
            if (c.getDeclaringClass().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public T newInstance(Object... initArgs) throws Throwable {
        List<Object> initArgsTmpList = new LinkedList<Object>(initArgsList);
        initArgsTmpList.addAll(Arrays.asList(initArgs));
        Object[] allInitArgs = initArgsTmpList.toArray();
        Class<?>[] initArgsTypes = getInitArgsTypes(allInitArgs);
        Constructor<T> constructor = constructorsMap.get(initArgsTypes);
        if (constructor == null) {
            if (initArgsTypes == null) {
                for (Constructor<T> c : constructors) {
                    if (allInitArgs.length == c.getParameterTypes().length) {
                        try {
                            T newInstance = (T) c.newInstance(allInitArgs);
                            constructorsMap.put(initArgsTypes, c);
                            return newInstance;
                        } catch (Exception e) {
                            throw checkForTargetException(e);
                        }
                    }
                }
            } else {
                for (Constructor<T> c : constructors) {
                    Class<?>[] paramTypes = c.getParameterTypes();
                    int i = 0;
                    boolean match = initArgsTypes.length == paramTypes.length;
                    if (match) {
                        for (Class<?> initArgsType : initArgsTypes) {
                            if (!paramTypes[i++].isAssignableFrom(initArgsType)) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            try {
                                T newInstance = (T) c.newInstance(allInitArgs);
                                constructorsMap.put(initArgsTypes, c);
                                return newInstance;
                            } catch (Exception e) {
                                throw checkForTargetException(e);
                            }
                        }                        
                    }
                }
            }
            throw new IllegalArgumentException(
                    "No suitable constructor found for arguments: " + Arrays.toString(initArgs));
        } else {
            try {
                return (T) constructor.newInstance(initArgs);
            } catch (Exception e) {
                throw checkForTargetException(e);
            }
        }
    }

    @Override
    public String toString() {
        return constructorsMap.toString();
    }

    private Class<?>[] getInitArgsTypes(Object... initArgs) {
        Class<?>[] initArgsTypes = new Class<?>[initArgs.length];
        int i = 0;
        for (Object arg : initArgs) {
            if (arg == null) {
                return null;
            } else {
                initArgsTypes[i++] = arg.getClass();
            }
        }
        return initArgsTypes;
    }

    private void addConstructors(Constructor<?>[] constructors) {
        for (Constructor<?> c : constructors) {
            c.setAccessible(true);
            this.constructors.add((Constructor<T>) c);
        }
    }

    private static Throwable checkForTargetException(Throwable t) throws Throwable {
        if (t instanceof InvocationTargetException) {
            return checkForTargetException(((InvocationTargetException) t).getTargetException());
        } else {
            return t;
        }
    }

    private Map<Class<?>[], Constructor<T>> constructorsMap = new HashMap<Class<?>[], Constructor<T>>();
    private Set<Constructor<T>> constructors = new HashSet<Constructor<T>>();
    private List<Object> initArgsList;

}

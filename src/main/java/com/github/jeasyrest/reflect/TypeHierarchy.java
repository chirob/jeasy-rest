package com.github.jeasyrest.reflect;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TypeHierarchy implements Iterable<Class<?>> {

    public static enum Mode {
        ALL, CLASS, INTERFACE
    }

    public TypeHierarchy(Class<?> type) {
        addAncestors(type, Mode.ALL);
    }

    public TypeHierarchy(Class<?> type, Mode mode) {
        addAncestors(type, mode);
    }

    public boolean contains(Class<?> type) {
        return ancestors.contains(type);
    }

    @Override
    public Iterator<Class<?>> iterator() {
        return ancestors.iterator();
    }

    private void addAncestors(Class<?> type, Mode mode) {
        if (type != null && !Object.class.equals(type)) {
            if (Mode.ALL.equals(mode) || (Mode.CLASS.equals(mode) && !type.isInterface())
                    || (Mode.INTERFACE.equals(mode) && type.isInterface())) {
                ancestors.add(type);
            }
            addAncestors(type.getSuperclass(), mode);            
            if (!(Mode.CLASS.equals(mode))) {
                for (Class<?> i : type.getInterfaces()) {
                    addAncestors(i, mode);
                }
            }
        }
    }

    private List<Class<?>> ancestors = new LinkedList<Class<?>>();

}

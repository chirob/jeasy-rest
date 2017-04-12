package com.github.jeasyrest.reflect;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TypeHierarchy implements Iterable<Class<?>> {

    public TypeHierarchy(Class<?> type) {
        addAncestors(type);
    }

    public boolean contains(Class<?> type) {
        return ancestors.contains(type);
    }

    @Override
    public Iterator<Class<?>> iterator() {
        return ancestors.iterator();
    }

    private void addAncestors(Class<?> type) {
        if (type != null && !Object.class.equals(type)) {
            ancestors.add(type);
            addAncestors(type.getSuperclass());
            for (Class<?> i: type.getInterfaces()) {
                addAncestors(i);    
            }
        }
    }

    private List<Class<?>> ancestors = new LinkedList<Class<?>>();

}

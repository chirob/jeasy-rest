package com.github.chirob.jeasyrest.reflect;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClassloaderResources implements Iterable<URL> {

    public ClassloaderResources(String name) {
        addResources(name, getClass().getClassLoader());
        addResources(name, Thread.currentThread().getContextClassLoader());
    }

    private void addResources(String name, ClassLoader cl) {
        try {
            Enumeration<URL> urls = cl.getResources(name);
            while (urls.hasMoreElements()) {
                resources.add(urls.nextElement());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Iterator<URL> iterator() {
        return resources.iterator();
    }

    private Set<URL> resources = new HashSet<URL>();

}

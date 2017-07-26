package com.github.jeasyrest.core.impl;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.github.jeasyrest.core.IHeaders;

public abstract class AbstractHeaders implements IHeaders {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Entry<String, Iterable<String>> entry : entrySet()) {
            if (sb.length() != 1) {
                sb.append(" - ");
            }
            sb.append(entry.getKey()).append("=");
            StringBuilder sbvalue = new StringBuilder();
            for (String value : entry.getValue()) {
                if (sbvalue.length() != 0) {
                    sbvalue.append(", ");
                }
                sbvalue.append(value);
            }
            sb.append(sbvalue);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterable<String> put(String key, String... values) {
        return put(key, Arrays.asList(values));
    }

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public Iterable<String> get(Object key) {
        for (Entry<String, Iterable<String>> entry : entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Iterable<String> put(String key, Iterable<String> value) {
        ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
        while (entryIterator.hasNext()) {
            Entry<String, Iterable<String>> entry = entryIterator.next();
            if (entry.getKey().equals(key)) {
                Iterable<String> oldValue = entry.getValue();
                entry.setValue(value);
                return oldValue;
            }
        }
        entryIterator.add(new AbstractMap.SimpleEntry<String, Iterable<String>>(key, value));
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Iterable<String>> m) {
        for (Entry<? extends String, ? extends Iterable<String>> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Iterable<String> remove(Object key) {
        ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
        while (entryIterator.hasNext()) {
            Entry<String, Iterable<String>> entry = entryIterator.next();
            if (entry.getKey().equals(key)) {
                Iterable<String> oldValue = entry.getValue();
                entryIterator.remove();
                return oldValue;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        entrySet().clear();
    }

    @Override
    public Set<String> keySet() {
        return new Set<String>() {
            @Override
            public int size() {
                return entrySet().size();
            }

            @Override
            public boolean isEmpty() {
                return entrySet().isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                while (entryIterator.hasNext()) {
                    Object value = entryIterator.next().getKey();
                    if (value == o || (value != null && value.equals(o))) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override
                    public String next() {
                        return entryIterator.next().getKey();
                    }

                    @Override
                    public void remove() {
                        entryIterator.remove();
                    }

                    private ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                };

            }

            @Override
            public Object[] toArray() {
                return null;
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(String e) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean remove(Object o) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                while (entryIterator.hasNext()) {
                    Object value = entryIterator.next().getKey();
                    if (value == o || (value != null && value.equals(o))) {
                        entryIterator.remove();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                for (Object o : c) {
                    boolean found = false;
                    while (entryIterator.hasNext()) {
                        Object value = entryIterator.next().getKey();
                        if (value == o || (value != null && value.equals(o))) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean addAll(Collection<? extends String> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                boolean changed = false;
                for (Object o : c) {
                    while (entryIterator.hasNext()) {
                        Object value = entryIterator.next().getKey();
                        if (!(value == o || (value != null && value.equals(o)))) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                }
                return changed;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                boolean changed = false;
                for (Object o : c) {
                    while (entryIterator.hasNext()) {
                        Object value = entryIterator.next().getKey();
                        if (value == o || (value != null && value.equals(o))) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                }
                return changed;
            }

            @Override
            public void clear() {
                entrySet().clear();
            }
        };
    }

    @Override
    public Collection<Iterable<String>> values() {
        return new Collection<Iterable<String>>() {
            @Override
            public int size() {
                return entrySet().size();
            }

            @Override
            public boolean isEmpty() {
                return entrySet().isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                while (entryIterator.hasNext()) {
                    Object value = entryIterator.next().getValue();
                    if (value == o || (value != null && value.equals(o))) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Iterator<Iterable<String>> iterator() {
                return new Iterator<Iterable<String>>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override
                    public Iterable<String> next() {
                        return entryIterator.next().getValue();
                    }

                    @Override
                    public void remove() {
                        entryIterator.remove();
                    }

                    private ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                };
            }

            @Override
            public Object[] toArray() {
                return null;
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Iterable<String> e) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean remove(Object o) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                while (entryIterator.hasNext()) {
                    Object value = entryIterator.next().getValue();
                    if (value == o || (value != null && value.equals(o))) {
                        entryIterator.remove();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                for (Object o : c) {
                    boolean found = false;
                    while (entryIterator.hasNext()) {
                        Object value = entryIterator.next().getValue();
                        if (value == o || (value != null && value.equals(o))) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean addAll(Collection<? extends Iterable<String>> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                boolean changed = false;
                for (Object o : c) {
                    while (entryIterator.hasNext()) {
                        Object value = entryIterator.next().getValue();
                        if (value == o || (value != null && value.equals(o))) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                }
                return changed;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                boolean changed = false;
                for (Object o : c) {
                    while (entryIterator.hasNext()) {
                        Object value = entryIterator.next().getValue();
                        if (!(value == o || (value != null && value.equals(o)))) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                }
                return changed;
            }

            @Override
            public void clear() {
                entrySet().clear();
            }

        };
    }

    @Override
    public Set<Entry<String, Iterable<String>>> entrySet() {
        return new Set<Entry<String, Iterable<String>>>() {
            @Override
            public int size() {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                int count = 0;
                while (entryIterator.hasNext()) {
                    count++;
                }
                return count;
            }

            @Override
            public boolean isEmpty() {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                return entryIterator.hasNext();
            }

            @Override
            public boolean contains(Object o) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                while (entryIterator.hasNext()) {
                    if (entryIterator.next().equals(o)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Iterator<Entry<String, Iterable<String>>> iterator() {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                return entryIterator;
            }

            @Override
            public Object[] toArray() {
                return null;
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Entry<String, Iterable<String>> e) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                entryIterator.add(e);
                return true;
            }

            @Override
            public boolean remove(Object o) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                while (entryIterator.hasNext()) {
                    if (entryIterator.next().equals(o)) {
                        entryIterator.remove();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                for (Object o : c) {
                    boolean found = false;
                    while (entryIterator.hasNext()) {
                        if (entryIterator.next().equals(o)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean addAll(Collection<? extends Entry<String, Iterable<String>>> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                for (Entry<String, Iterable<String>> e : c) {
                    entryIterator.add(e);
                }
                return true;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                boolean changed = false;
                for (Object o : c) {
                    while (entryIterator.hasNext()) {
                        if (!entryIterator.next().equals(o)) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                }
                return changed;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                boolean changed = false;
                for (Object o : c) {
                    while (entryIterator.hasNext()) {
                        if (entryIterator.next().equals(o)) {
                            entryIterator.remove();
                            changed = true;
                        }
                    }
                }
                return changed;
            }

            @Override
            public void clear() {
                ListIterator<Entry<String, Iterable<String>>> entryIterator = entryIterator();
                while (entryIterator.hasNext()) {
                    entryIterator.remove();
                }
            }

        };
    }

    protected abstract ListIterator<Entry<String, Iterable<String>>> entryIterator();

}

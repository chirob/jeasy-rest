package com.github.jeasyrest.core.http;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.github.jeasyrest.core.impl.AbstractHeaders;

public class RemoteHeaders extends AbstractHeaders {

    @Override
    protected ListIterator<Entry<String, Iterable<String>>> entryIterator() {
        return new ListIterator<Entry<String, Iterable<String>>>() {
            @Override
            public boolean hasNext() {
                return headers.hasNext();
            }

            @Override
            public Entry<String, Iterable<String>> next() {
                current = new Entry<String, Iterable<String>>() {
                    @Override
                    public String getKey() {
                        return next.getKey();
                    }

                    @Override
                    public Iterable<String> getValue() {
                        return next.getValue();
                    }

                    @Override
                    public Iterable<String> setValue(Iterable<String> value) {
                        String key = next.getKey();
                        StringBuilder sb = new StringBuilder();
                        for (String v : value) {
                            if (sb.length() != 0) {
                                sb.append(",");
                            }
                            sb.append(v);
                        }
                        connection.setRequestProperty(key, sb.toString());

                        Map<String, List<String>> headerMap = requestHeaders ? connection.getRequestProperties()
                                : connection.getHeaderFields();
                        return headerMap.get(key);
                    }

                    private Entry<String, List<String>> next = headers.next();
                };
                return current;
            }

            @Override
            public void remove() {
                if (requestHeaders) {
                    connection.setRequestProperty(current.getKey(), "");                    
                } else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public void add(Entry<String, Iterable<String>> e) {
                if (requestHeaders) {
                    for (String value : e.getValue()) {
                        connection.addRequestProperty(e.getKey(), value);
                    }                    
                } else {
                    throw new UnsupportedOperationException();                    
                }
            }

            @Override
            public boolean hasPrevious() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Entry<String, Iterable<String>> previous() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int nextIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int previousIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(Entry<String, Iterable<String>> e) {
                throw new UnsupportedOperationException();
            }

            private Map<String, List<String>> headerMap = requestHeaders ? connection.getRequestProperties()
                    : connection.getHeaderFields();
            private Iterator<Entry<String, List<String>>> headers = headerMap.entrySet().iterator();
        };
    }

    protected RemoteHeaders(boolean requestHeaders, HttpURLConnection connection) {
        this.requestHeaders = requestHeaders;
        this.connection = connection;
    }

    private boolean requestHeaders;
    private HttpURLConnection connection;
    private Entry<String, Iterable<String>> current;
}
